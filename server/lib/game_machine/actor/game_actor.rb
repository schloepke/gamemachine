require_relative '../commands'
require_relative '../model'
require_relative '../models'
module GameMachine
  module Actor
    class GameActor < Base
      include GameMachine::Commands
      include GameMachine::Models
      attr_reader :player_id

      def awake(args)

      end

      def post_init(*args)
        awake(args)
        commands.misc.client_manager_register(self.class.name)
      end

      def send_game_message(game_message,playerid=player_id)
        commands.player.send_game_message(game_message,playerid)
      end

      def on_receive(message)
        @player_id = nil

        if has_game_messages?(message)
          @player_id = message.player.id
          game_messages(message).each {|m| on_game_message(m)}
        elsif message.is_a?(MessageLib::ClientManagerEvent)
          if message.event == 'disconnected'
            if self.respond_to?(:on_player_disconnect)
              on_player_disconnect(message.player_id)
            end
          end
        end
      end

      def has_game_messages?(message)
        message.is_a?(MessageLib::Entity) && message.has_game_messages
      end
      
      def game_messages(message)
        message.game_messages.get_game_message_list
      end

    end
  end
end
