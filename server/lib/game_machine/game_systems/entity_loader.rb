module GameMachine
  module GameSystems
    class EntityLoader < Actor::Base

      def on_receive(entity_list)
        entity_list.entity.each do |entity|
          ObjectDb.put(entity.id,entity)
        end
      end
    end
  end
end
