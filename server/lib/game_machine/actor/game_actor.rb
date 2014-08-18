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

      def on_player_disconnect(player_id)
      end

      def post_init(*args)
        awake(args)
        commands.misc.client_manager_register(self.class.name)
      end

      def call_mono(klass,message)
        commands.misc.call_mono(klass,message)
      end

      def send_game_message(game_message,playerid=player_id)
        commands.player.send_game_message(game_message,playerid)
      end

      def on_receive(message)
        @player_id = nil

        if message.is_a?(MessageLib::GameMessage)
          @player_id = message.player_id
          set_player_id(message.player_id)
          on_game_message(message)
        elsif message.is_a?(MessageLib::ClientManagerEvent)
          if message.event == 'disconnected'
            on_player_disconnect(message.player_id)
          end
        end
      end

    end
  end
end
