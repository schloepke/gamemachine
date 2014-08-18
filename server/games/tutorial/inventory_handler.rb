require_relative 'object_store'
require_relative 'sql_store'

module Tutorial
  class InventoryHandler < GameMachine::Actor::GameActor
    include GameMachine
    attr_reader :player_item_cache, :catalog_list, :catalog_map, :object_store, :sql_store

    def awake(args)
      @sql_store = SqlStore.new
      @object_store = ObjectStore.new

      @player_item_cache = {}

      @catalog_list = MessageLib::PlayerItem.orm_find_all('global')
      @catalog_map = catalog_list.each_with_object({}) {|v,res| res[v.id] = v}
    end

    def on_game_message(game_message)
      GameMachine.logger.info "Inventory handler got message"
      if exactly_once(game_message)
        if game_message.has_add_player_item
          if player_item = add_player_item(game_message.add_player_item.player_item)
            game_message.add_player_item.set_player_item(player_item)
            set_reply(game_message)
          end
        end

        if game_message.has_remove_player_item
          remove_player_item(
            game_message.remove_player_item.id,
            game_message.remove_player_item.quantity
          )
          set_reply(game_message)
        end
      end
    

      if game_message.has_request_player_items
        player_items_message = MessageLib::PlayerItems.new

        if game_message.request_player_items.catalog
          player_items_message.set_player_item_list(catalog_list)
          player_items_message.catalog = true
        else
          player_items.values.each {|pi| player_items_message.add_player_item(pi)}
        end
        
        player_message = MessageLib::GameMessage.new
        player_message.set_player_items(player_items_message)
        send_game_message(player_message)
      end
    end

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
      if current_player_item = player_items.fetch(player_item.id,nil)
        current_player_item.quantity += player_item.quantity
      else
        if catalog_item = catalog_map.fetch(player_item.id)
          current_player_item = catalog_item.clone
          current_player_item.quantity = player_item.quantity
          current_player_item.name = player_item.name
          player_items[current_player_item.id] = current_player_item
        else
          return nil
        end
      end
      
      store_save_player_item(current_player_item)
      current_player_item
    end

    def remove_player_item(id,quantity)
      if player_item = player_items.fetch(id,nil)
        player_item.quantity -= quantity
        if player_item.quantity >= 1
          store_save_player_item(player_item)
        else
          store_delete_player_item(player_item)
          player_items.delete(id)
        end
      end
    end

    def on_player_disconnect(player_id)
      player_item_cache.delete(player_id)
    end

    private

    def store_save_player_item(player_item)
      if player_item.has_consumable
        object_store.save_player_item(player_item,player_id)
      else
        sql_store.save_player_item(player_item,player_id)
      end
    end

    def store_delete_player_item(player_item)
      if player_item.has_consumable
        object_store.delete_player_item(player_item.id,player_id)
      else
        sql_store.delete_player_item(player_item.id,player_id)
      end
    end
    
  end
end