module GameMachine
  module Systems
    class EntityDispatcher < Actor 

      def on_receive(message)
        if message.is_a?(ClientMessage)
          dispatch_entities(message.get_entity_list)
        else
          unhandled(message)
        end
      end

      private

      def dispatch_entities(entities)
        entities.each do |entity|
          component_names = entity.component_names
          next if component_names.empty?
          SystemManager.registered.each do |klass|
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

