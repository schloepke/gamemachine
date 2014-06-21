module GameMachine
  module Commands
    class MiscCommands

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
