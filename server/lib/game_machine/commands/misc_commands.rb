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

      def client_manager_register(name,events=[])
        register = MessageLib::ClientManagerRegister.new.
          set_register_type('actor').set_name(name).set_events(events.join('|'))
        entity = MessageLib::Entity.new.set_id(name).
          set_client_manager_register(register)
        ClientManager.find.tell(entity)
      end

    end
  end
end
