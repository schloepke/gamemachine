module Tutorial
  class InventoryHandler < GameMachine::Actor::GameActor
    include GameMachine
    attr_reader :player_items_cache, :player_items_list, :player_items_map

    def awake(args)

      # Keep local caches of everything so we are only hitting the db when necessary
      @player_items_cache = {}
      @player_items_list = MessageLib::PlayerItem.orm_find_all('global')
      @player_items_map = player_items_list.each_with_object({}) {|v,res| res[v.id] = v}
    end

    def on_player_disconnect(player_id)

      # If player disconnects remove their cache.  Always clear local copies of user specific data.
      # It's unlikely that a user will connect to another node and then back to this one, but
      # it is possible.
      player_items_cache.delete(player_id)
    end

    def player_items
      unless player_items_cache.fetch(player_id,nil)
        player_items_cache[player_id] = {}
        MessageLib::PlayerItem.orm_find_all(player_id).each do |player_item|
          player_items_cache[player_id][player_item.id] = player_item
        end
      end
      player_items_cache[player_id]
    end

    def add_player_item(player_item)
      if current_player_item = player_items.fetch(player_item.id,nil)
        current_player_item.quantity += player_item.quantity
      else
        # If item id is valid, then clone the global item into the player item,
        # and set the id, name, and quantity to those sent in player_item.
        if current_player_item = player_items_map.fetch(player_item.id)
          current_player_item = current_player_item.clone
          current_player_item.quantity = player_item.quantity
          current_player_item.name = player_item.name
          player_items[current_player_item.id] = current_player_item
        else
          return nil
        end
      end
      current_player_item.orm_save_async(player_id)
      current_player_item
    end

    def remove_player_item(id,quantity)
      if player_item = player_items.fetch(id,nil)
        player_item.quantity -= quantity
        if player_item.quantity >= 1
          player_item.orm_save_async(player_id)
        else
          MessageLib::PlayerItem.orm_delete_async(id,player_id)
          player_items.delete(id)
        end
      end
    end


    def on_game_message(game_message)

      # Use reliable messaging for add/remove
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
          player_items_message.set_player_item_list(player_items_list)
          player_items_message.catalog = true
        else
          player_items.values.each {|pi| player_items_message.add_player_item(pi)}
        end
        
        player_message = MessageLib::GameMessage.new
        player_message.set_player_items(player_items_message)
        send_game_message(player_message)
      end
      
    end
  end
end