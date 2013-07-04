module GameMachine
  module Systems
    class EntityDispatcher < Actor 

      def post_init(*args)
        @entity_managers = {}
      end

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
          if @entity_managers[entity.class.name]
            @entity_managers[entity.class.name].each do |klass|
                klass.find.send_message(entity)
            end
          else
            SystemManager.registered.each do |klass|
              next if klass.components.empty?
              if (klass.components & entity.component_names).size == klass.components.size
                @entity_managers[entity.class.name] ||= []
                @entity_managers[entity.class.name] << klass
                klass.find.send_message(entity)
              end
            end
          end
        end
      end

    end
  end
end

