module GameMachine
  module Handlers
    class Authentication < Actor::Base
      attr_reader :state_machine

      def post_init
        @state_machines = {}
      end

      def get_machine(player_id)
        @state_machines[player_id] ||= AuthenticationMachine.new
      end

      def on_receive(message)
        if message.is_a?(Disconnected)
          GameMachine.logger.debug "RequestHandlers::Authentication Disconnected #{message.player_id}"
          @state_machines.delete(message.player_id)
        else
          @state_machine = get_machine(message.player.id)
          @state_machine.run(message,get_self)
        end
      end

    end

    class AuthenticationMachine
      include AASM

      attr_reader :message, :actor_ref
      def run(message,actor_ref)
        @message = message
        @actor_ref = actor_ref

        if authenticated? || authenticate
          handler.tell(message)
        end
        message.player.authenticated = authenticated?
      end

      aasm :whiny_transitions => false

      aasm do
        state :unauthenticated, :initial => true
        state :authenticated

        event :authenticate do
          after do
            register_player
          end
          transitions :from => :unauthenticated, :to => :authenticated, :guard => :valid_authtoken?
        end
      end

      def register_player
        player_register = PlayerRegister.new.
          set_client_connection(message.client_connection).
          set_player_id(message.player.id).
          set_observer(actor_ref.path.name)
        PlayerRegistry.find.ask(player_register,100)
      end

      def handler
        Handlers::Game.find
      end

      def valid_authtoken?
        message.player.authtoken == Settings.authtoken
      end

    end
  end
end
