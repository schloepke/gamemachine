module Tutorial
  class TutorialHandler < GameMachine::Actor::GameActor

    def on_game_message(game_message)

      if message.has_get_player_items
        InventoryHandler.find.tell(game_message)
      end

    end
  end
end