module GameMachine
  module Handlers
    class Request < Actor::Base

      def on_receive(message)
        if message.is_a?(MessageLib::ClientMessage)
          if message.has_player_logout
            if Authentication.authenticated?(message.player)
              GameMachine::GameSystems::PlayerManager.find.tell(message)
            end
          elsif message.has_client_disconnect
            GameMachine::GameSystems::PlayerManager.find.tell(message)
          elsif message.has_player
            update_entities(message)
            if Authentication.authenticated?(message.player)
              game_handler.tell(message)
            else
              authenticate_player(message)
            end
          else
            unhandled(message)
          end
        else
          unhandled(message)
        end
      end

      private

      def game_handler
        @game_handler ||= Handlers::Game.find
      end

      def update_entities(message)
        if message.get_entity_list
          message.get_entity_list.each do |entity|
            entity.set_player(message.player)
          end
        end
      end

      def authenticate_player(message)
        Authentication.find_distributed_local(
          message.player.id
        ).tell(message,self)
      end

    end
  end
end
