module GameMachine
  module Systems
    class EntityDispatcher < Actor 

      def on_receive(message)
        if message.is_a?(EntityList)
          dispatch_entities(message.get_entity_list)
        else
          unhandled(message)
        end
      end

      private

      def dispatch_entities(entities)
        entities.each do |entity|
          SystemManager.registered.each do |klass|
            next if klass.components.empty?
            if (klass.components & entity.component_names).size == klass.components.size
              klass.find.send_message(entity)
            end
          end
        end
      end

    end
  end
end

