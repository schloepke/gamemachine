module GameMachine
  class CommandRouter < GameActor 

    def on_receive(message)
      GameMachine.logger.debug "CommandRouter got #{message}"

      entities = EntityList.parse_from(message.data).get_entity_list
      client_id = message.client_id

      entities.each do |entity|
        GameActor.systems.each do |klass|
          next if klass.components.empty?
          if (klass.components & entity.component_names).size == klass.components.size
            entity.set_client_id(message.client_id)
            klass.find.send_message(entity)
          end
        end
      end
    end

  end
end

