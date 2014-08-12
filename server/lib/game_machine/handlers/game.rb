module GameMachine
  module Handlers
    class Game < Actor::Base 
      include Commands

      attr_reader :destinations, :game_message_handler
      def post_init(*args)
        @destinations = {}
        if Application.config.game_message_handler
          @game_message_handler = Actor::Base.find(Application.config.game_message_handler)
        end
      end


      def on_receive(message)
        if message.is_a?(MessageLib::ClientMessage)
          if message.get_entity_list
            dispatch_entities(message.get_entity_list)
          end
        else
          unhandled(message)
        end
      end

      private

      def dispatch_entities(entities)
        entities.each do |entity|
          if entity.save
            entity = GameSystems::ObjectDbProxy.save_entity(entity)
          end

          if entity.has_game_messages
            if game_message_handler
              game_message_handler.tell(entity)
            end
          end

          if entity.has_destination
            unless actor_ref = destinations.fetch(entity.destination,nil)
              destination = entity.destination.gsub('/','::')
              actor_ref = Actor::Base.find(destination)
              destinations[entity.destination] = actor_ref
            end
            entity.set_destination(nil)
            actor_ref.tell(entity)
            GameMachine.logger.debug("RouteToDestination: #{entity.id} #{destination}")
            next
          end

          # TODO: Optimize this
          component_names = entity.component_names
          GameMachine.logger.debug("Dispatch: #{entity} #{component_names.to_a.inspect}")
          next if component_names.empty?
          Application.registered.each do |klass|
            dispatched = false
            klass.aspects.each do |aspect|
              next if dispatched
              if (aspect & component_names).size == aspect.size
                GameMachine.logger.debug "Routing #{entity} via #{aspect} #{component_names} to #{klass}"
                klass.find.tell(entity)
                dispatched = true
              end
            end
          end
        end
      end

    end
  end
end

