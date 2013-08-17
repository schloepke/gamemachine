require 'state_machine'

module GameMachine
  module Handlers
    class Authentication < Actor::Base
      include Helpers::StateMachine

      state_machine :state, :initial => :unauthenticated do
        event :authenticate do
          transition :unauthenticated => :authenticated, :if => :valid_authtoken?
        end

        after_transition :unauthenticated => :authenticated, :do => :register_player
      end

      def register_player
        player_register = PlayerRegister.new.
          set_client_connection(@message.client_connection).
          set_player_id(@message.player.id).
          set_observer(get_self.path.name)
        PlayerRegistry.find.ask(player_register,100)
      end

      def valid_authtoken?
        @message.player.authtoken == Settings.authtoken
      end

      def on_receive(message)
        @message = message
        if message.is_a?(Disconnected)
          GameMachine.logger.debug "RequestHandlers::Authentication Disconnected #{message.player_id}"
          destroy_state(message.player_id)
        else
          load_state(message.player.id) do
            if authenticated? || authenticate
              Handlers::Game.find.tell(message)
            end
            message.player.authenticated = authenticated?
          end
        end
      end
    end

  end
end
