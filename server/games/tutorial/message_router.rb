module Tutorial

  # All game messages are sent here
  class MessageRouter < GameMachine::Actor::GameActor

    def on_game_message(game_message)
      return
      if game_message.has_request_player_items ||
        game_message.has_add_player_item ||
        game_message.has_remove_player_item

        InventoryHandler.find_distributed(player_id).tell(game_message)
      end

    end
  end
end