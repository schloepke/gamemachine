module GameMachine
  class CommandRouter < GameActor 

    def on_receive(message)
      GameMachine.logger.debug "CommandRouter got #{message}"

      entities = EntityList.parse_from(message.data).get_entity_list
      client_connection = message.client_connection

      entities.each do |entity|
        GameActor.systems.each do |klass|
          next if klass.components.empty?
          if (klass.components & entity.component_names).size == klass.components.size
            entity.set_client_connection(message.client_connection)
            klass.find.send_message(entity)
          end
        end
      end
    end

  end
end

