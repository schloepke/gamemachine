module GameMachine
  class CommandRouter < GameActor 

    def on_receive(gateway_message)
      GameMachine.logger.info "CommandRouter got #{gateway_message}"

      entities = EntityList.parse_from(gateway_message.get_bytes).get_entity_list
      client_id = gateway_message.get_client_id

      entities.each do |entity|
        GameActor.systems.each do |klass|
          next if klass.components.empty?
          if (klass.components & entity.component_names).size == klass.components.size
            entity.set_client_id(ClientId.new.set_id(client_id))
            ref = klass.find
            ref.send_message(entity)
          end
        end
      end
    end

  end
end

