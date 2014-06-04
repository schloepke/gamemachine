module GameMachine
  module GameSystems
    class ObjectDbProxy < Actor::Base
      include GameMachine::Commands

      aspect %w(ObjectdbGet Player)

      # Generic way for a client to save state.  We scope the id to the
      # player to prevent hacked clients from writing stuff they shouldn't.


      def self.save_entity(entity)
        scoped_id = scope_entity_id(entity.id,entity.player.id)
        entity.set_id(scoped_id)
        entity.set_save(false)
        GameMachine.logger.info "ObjectDbProxy save scoped_id #{scoped_id}"
        ref = ObjectDb.find_distributed(entity.get_id)
        ref.tell(MessageLib::ObjectdbPut.new.set_entity(entity))
        entity
      end

      def self.scope_entity_id(entity_id,player_id)
        "#{player_id}_#{entity_id}"
      end

      def self.unscope_entity_id(entity_id,player_id)
        unscoped_id = entity_id.sub("#{player_id}_",'')
      end

      def on_receive(message)
        if message.is_a?(MessageLib::Entity)
          if message.has_objectdb_get
            objectdb_get = message.get_objectdb_get
            entity_id = objectdb_get.get_entity_id
            player_id = objectdb_get.get_player_id

            response = MessageLib::ObjectdbGetResponse.new

            # Make request with scoped id
            scoped_id = self.class.scope_entity_id(entity_id,player_id)
            GameMachine.logger.info "ObjectDbProxy scoped_id #{scoped_id}"
            if entity = commands.datastore.get(scoped_id)

              # Unscope the entity id
              unscoped_id = self.class.unscope_entity_id(entity.id,player_id)
              GameMachine.logger.info "ObjectDbProxy unscoped_id #{unscoped_id}"
              entity.set_id(unscoped_id)

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
