module GameMachine
  module Handlers
    class Game < Actor::Base 
      include Commands

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
          if entity.has_destination
            destination = entity.destination.gsub('/','::')
            entity.set_destination(nil)
            GameMachine.logger.debug("RouteToDestination: #{entity.id} #{destination}")
            Actor::Base.find(destination).tell(entity)
            next
          end
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

