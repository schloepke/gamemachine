module GameMachine
  module Commands
    class MiscCommands

      attr_reader :player_manager, :mono_proxy
      def initialize
        @mono_proxy = JavaLib::MonoProxy.getInstance
        @player_manager = Application.config.player_manager
      end

      def call_mono(klass,message)
        mono_proxy.call(klass,message)
      end

      def player_status_change(player_id,status)
        if player_manager
          update = Models::PlayerStatusUpdate.new(:player_id => player_id,:status => status)
          Actor::Base.find(player_manager).tell(update)
        else
          GameMachine.logger.info "player manager not defined."
        end
      end

      def client_manager_register(name,events=[])
        register = MessageLib::ClientManagerRegister.new.
          set_register_type('actor').set_name(name).set_events(events.join('|'))
        entity = MessageLib::Entity.new.set_id(name).
          set_client_manager_register(register)
        ClientManager.find.ask(entity,5000)
      end

    end
  end
end
