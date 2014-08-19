require_relative 'object_store'
require_relative 'sql_store'

module Tutorial

  # Provides item persistence and management.  Choice of using object store or sql on a per item basis.
  # The choice of what store you use is up to you.  Adding sql items can be expensive, especially for limited
  # quantity or items that have a cost, as we use synchronous queries and transactions.  Sql delets are asynchronous,
  # as are all object store operations.

  # This demo is designed to show the full persistence api and reliable messaging in a common use case.  It should work
  # fine as a starting point for your own item management.

  # Design notes:
  # - Item definitions are just items assigned to the 'global' user.  Player items are created by cloning one of the global items.
  #
  # - Extending the system for more types of items should be done compositionally by adding new messages to PlayerItem.  Think of it
  # as an entity component system where PlayerItem is the entity, and all the messages it contains are the components.
  #
  # - We prefer to use nested messages instead of adding concrete fields to PlayerItem.  It's a good way to keep encapsulation.

  class ItemManager < GameMachine::Actor::GameActor
    include GameMachine
    attr_reader :player_item_cache, :catalog_list, :catalog_map, :object_store, :sql_store, :catalog_user

    def awake(args)
      @catalog_user = 'global'
      @sql_store = SqlStore.new
      @object_store = ObjectStore.new

      @player_item_cache = {}

      @catalog_list = MessageLib::PlayerItem.orm_find_all(catalog_user)
      @catalog_map = catalog_list.each_with_object({}) {|v,res| res[v.id] = v}
    end

    def on_game_message(game_message)
      player_items_message = MessageLib::PlayerItems.new

      # exactly_once returns true of the message is a reliable message, and it is the first time we have seen
      # the message.  Use set_reply instead of tell_player for reliable messages. 
      if exactly_once(game_message)
        reply = new_game_message.set_player_items(player_items_message)

        if game_message.has_add_player_item
          add_player_item(game_message.add_player_item.player_item).each do |player_item|
            reply.player_items.add_player_item(player_item)
          end
        end

        if game_message.has_remove_player_item
          if player_item = remove_player_item(game_message.remove_player_item.id,
            game_message.remove_player_item.quantity
          )
            reply.player_items.add_player_item(player_item)
          end
        end

        set_reply(reply)

      elsif game_message.has_request_player_items

        if game_message.request_player_items.catalog
          player_items_message.set_player_item_list(catalog_list)
          player_items_message.catalog = true
        else
          player_items.values.each {|pi| player_items_message.add_player_item(pi)}
        end
        
        player_message = new_game_message
        player_message.set_player_items(player_items_message)
        tell_player(player_message)
      else
        unhandled(game_message)
      end
    end

    def on_player_disconnect(player_id)
      player_item_cache.delete(player_id)
    end

    private

    def player_items
      unless player_item_cache.fetch(player_id,nil)
        player_item_cache[player_id] = {}
        sql_store.all_for_player(player_id).each do |player_item|
          player_item_cache[player_id][player_item.id] = player_item
        end
        object_store.all_for_player(player_id).each do |player_item|
          player_item_cache[player_id][player_item.id] = player_item
        end
      end
      player_item_cache[player_id]
    end

    def add_player_item(player_item)
      catalog_item = catalog_map.fetch(player_item.id,nil)
      return nil if catalog_item.nil?

      if object_store_item?(player_item)
        return add_object_store_item(player_item,catalog_item)
      else
        return add_sql_item(player_item,catalog_item)
      end
    end

    def remove_player_item(id,quantity)
      if player_item = player_items.fetch(id,nil)
        player_item.quantity -= quantity
        if player_item.quantity >= 1
          store_save_player_item(player_item,player_id)
        else
          store_delete_player_item(player_item,player_id)
          player_items.delete(id)
        end
        player_item
      end
    end

    # Retrieves current player item, or creates new one from catalog item
    # new items are set to 0 quantity and stripped of components that we don't need
    # to store on the player copy, such as the cost.
    def player_item(id,catalog_item)
      if current_player_item = player_items.fetch(id,nil)
        current_player_item
      else
        current_player_item = catalog_item.clone
        current_player_item.quantity = 0
        current_player_item.set_cost(nil)
        current_player_item
      end
    end

    def deduct_item_cost(player_item,catalog_item)
      currency = catalog_item.cost.currency
      return nil unless player_items.has_key?(currency)

      player_currency = player_items[currency].clone
      
      currency_required = player_item.quantity * catalog_item.cost.amount
      if player_currency.quantity >= currency_required
        player_currency.quantity -= currency_required
        store_save_player_item(player_currency,player_id,true)
        return player_currency
      else
        nil
      end
    end

    def add_object_store_item(player_item,catalog_item)
      current_player_item = player_item(player_item.id,catalog_item)
      current_player_item.quantity += player_item.quantity
      player_items[current_player_item.id] = current_player_item

      store_save_player_item(current_player_item,player_id)
      return [current_player_item]
    end

    def add_sql_item(player_item,catalog_item)
      state = OpenStruct.new
      state.player_item = player_item(player_item.id,catalog_item).clone

      begin
        ModelLib::PlayerItem.open_transaction

        limited_item = catalog_item.quantity == -1 ? false : true

        if limited_item
          state.catalog_item = sql_store.find_by_id(catalog_item.id,catalog_user,true)
          if state.catalog_item.quantity >= player_item.quantity
            state.catalog_item.quantity -= player_item.quantity
            store_save_player_item(state.catalog_item,catalog_user,true)
          else
            ModelLib::PlayerItem.rollback_transaction
            GameMachine.logger.info "Insufficient quantity (#{current_catalog_item.quantity}) for #{player_item.id}"
            return []
          end
        end

        if catalog_item.has_cost
          state.player_currency = deduct_item_cost(player_item,catalog_item)
          if state.player_currency.nil?
            ModelLib::PlayerItem.rollback_transaction
            GameMachine.logger.info "cost deduction failed #{player_item.id}"
            return []
          end
        end

        state.player_item.quantity += player_item.quantity
        store_save_player_item(state.player_item,player_id,true)
        
        ModelLib::PlayerItem.commit_transaction

        player_items[state.player_item.id] = state.player_item
        
        if state.catalog_item
          catalog_item = state.catalog_item
        end

        if state.player_currency
          player_items[state.player_currency.id] = state.player_currency
        end

        return [state.player_item,state.player_currency].compact
      rescue Exception => e
        GameMachine.logger.error "Error adding sql item #{player_item.id} #{e.to_s} \n #{e.backtrace.join("\n")}"
        ModelLib::PlayerItem.rollback_transaction
      end
    end

    # Use object store when:
    # - item is not currency
    # - item is not limited quantity
    # - item does not have a cost
    def object_store_item?(player_item)
      catalog_item = catalog_map.fetch(player_item.id)
      if catalog_item.has_cost
        false
      elsif ['gold','silver'].include?(catalog_item.id)
        false
      elsif catalog_item.quantity = -1
        true
      else
        false
      end
    end

    def store_save_player_item(player_item,player_id,in_transaction=false)
      if object_store_item?(player_item)
        object_store.save_player_item(player_item,player_id)
      else
        sql_store.save_player_item(player_item,player_id,in_transaction)
      end
    end

    def store_delete_player_item(player_item,player_id)
      if object_store_item?(player_item)
        object_store.delete_player_item(player_item.id,player_id)
      else
        sql_store.delete_player_item(player_item.id,player_id)
      end
    end
    
  end
end