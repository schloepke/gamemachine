module Tutorial
  class InventoryHandler < GameMachine::Actor::GameActor
    include GameMachine
    attr_reader :player_items_cache, :player_items_catalog

    def awake(args)
      @player_items_cache = {}
      @player_items_catalog = MessageLib::PlayerItem.orm_find_all('global')
    end

    def on_player_disconnect(player_id)
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
        current_player_item = player_item
        player_items[current_player_item.id] = current_player_item
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

      if game_message.has_add_player_item
        player_item = add_player_item(game_message.add_player_item.player_item)
        game_message.add_player_item.set_player_item(player_item)
        send_game_message(game_message)
      end

      if game_message.has_remove_player_item
        remove_player_item(
          game_message.remove_player_item.id,
          game_message.remove_player_item.quantity
        )
        send_game_message(game_message)
      end

      if game_message.has_get_player_items_catalog
        player_items_message = MessageLib::PlayerItems.new
        player_items_message.set_player_item_list(player_items_catalog)
        player_items_message.catalog = true
        player_message = MessageLib::GameMessage.new
        player_message.set_player_items(player_items_message)
        send_game_message(player_message)
      end

      if game_message.has_get_player_items
        player_items_message = MessageLib::PlayerItems.new
        player_items.values.each {|pi| player_items_message.add_player_item(pi)}
        player_message = MessageLib::GameMessage.new
        player_message.set_player_items(player_items_message)
        send_game_message(player_message)
      end
      
    end
  end
end