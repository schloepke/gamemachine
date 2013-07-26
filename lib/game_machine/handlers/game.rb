module GameMachine
  module Handlers
    class Game < Actor::Base 

      def on_receive(message)
        if message.is_a?(ClientMessage)
          if message.get_entity_list
            dispatch_entities(message.get_entity_list)
          end
        else
          unhandled(message)
        end
      end

      private

      def registered_classes
        Application.registered
      end

      def dispatch_entities(entities)
        entities.each do |entity|
          component_names = entity.component_names
          GameMachine.logger.debug("Dispatch: #{entity} #{component_names.to_a.inspect}")
          next if component_names.empty?
          registered_classes.each do |klass|
            klass.aspects.each do |aspect|
              if (aspect & component_names).size == aspect.size
                klass.find.tell(entity)
              end
            end
          end
        end
      end

    end
  end
end

