require 'state_machine'

module GameMachine
  module Handlers
    class Authentication < Actor::Base
      AUTHTOKEN = 'authorized'
      AUTHENTICATED_USERS = java.util.concurrent.ConcurrentHashMap.new
      include Helpers::StateMachine

      state_machine :state, :initial => :unauthenticated do
        event :authenticate do
          transition :unauthenticated => :authenticated, :if => :valid_authtoken?
        end

        after_transition :unauthenticated => :authenticated, :do => :register_player
      end

      def self.authenticated?(player_id)
        AUTHENTICATED_USERS.has_key?(player_id)
      end

      def notify_player_controller(player)
        entity = Entity.new.set_id(player.id)
        entity.set_player_authenticated(
          PlayerAuthenticated.new.set_player_id(
            player.id
          )
        ).set_player(player)
        GameSystems::PlayerManager.find.tell(entity)
      end

      def self.unregister_player(player_id)
        AUTHENTICATED_USERS.delete(player_id)
      end

      def register_player
        AUTHENTICATED_USERS[@message.player.id] = true
        player_register = PlayerRegister.new.
          set_client_connection(@message.client_connection).
          set_player_id(@message.player.id).
          set_observer(get_self.path.name)
        PlayerRegistry.find.ask(player_register,100)
        notify_player_controller(@message.player)
      end

      def valid_authtoken?
        @message.player.authtoken == 'authorized'
      end

      def send_to_game_handler(message)
        Handlers::Game.find.tell(message)
      end

      def on_receive(message)
        @message = message
        if message.is_a?(Disconnected)
          GameMachine.logger.debug "RequestHandlers::Authentication Disconnected #{message.player_id}"
          destroy_state(message.player_id)
        else
          load_state(message.player.id) do
            if authenticated? || authenticate
              send_to_game_handler(message)
            end
            message.player.authenticated = authenticated?
          end
        end
      end
    end

  end
end
