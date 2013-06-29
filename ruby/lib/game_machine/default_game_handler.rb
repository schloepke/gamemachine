module GameMachine
  class DefaultGameHandler < GameActor 

    def post_init(*args)
    end

    def on_receive(message)
      GameMachine.logger.debug "CommandRouter got #{message}"

      if message.is_a?(ClientMessage)
        entity_list = client_message_to_entity_list(message)
        PlayerAuthentication.find.send_message(entity_list,:sender => get_self)
      elsif message.is_a?(EntityList)
        dispatch_entities(message.get_entity_list)
      else
        unhandled(message)
      end

    end

    private

    def reset_player_authentication(player)
      player.set_authenticated(false)
    end

    def client_message_to_entity_list(message)
      entity_list = EntityList.parse_from(message.data)
      reset_player_authentication(entity_list.get_player)
      entity_list.get_entity_list.each do |entity|
        entity.set_client_connection(message.client_connection)
      end
      entity_list
    end

    def dispatch_entities(entities)
      entities.each do |entity|
        GameActor.systems.each do |klass|
          next if klass.components.empty?
          if (klass.components & entity.component_names).size == klass.components.size
            klass.find.send_message(entity)
          end
        end
      end
    end

  end
end

