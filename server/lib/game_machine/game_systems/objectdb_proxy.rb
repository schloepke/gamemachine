module GameMachine
  module GameSystems
    class ObjectDbProxy < Actor::Base
      include GameMachine::Commands

      aspect %w(ObjectdbGet Player)

      def on_receive(message)
        GameMachine.logger.debug "ObjectDbProxy got #{message}"
        if message.is_a?(MessageLib::Entity)
          if message.has_objectdb_get
            objectdb_get = message.get_objectdb_get
            entity_id = objectdb_get.get_entity_id
            player_id = objectdb_get.get_player_id

            response = MessageLib::ObjectdbGetResponse.new
            if entity = commands.datastore.get(entity_id)
              response.set_entity_found(true)
            else
              entity = MessageLib::Entity.new.set_id(entity_id)
              response.set_entity_found(false)
            end
            entity.set_objectdb_get_response(response)
            commands.player.send_message(entity,player_id)
            GameMachine.logger.info "ObjectDbProxy send #{entity} to #{player_id}"
          end
        end
      end

    end
  end
end
