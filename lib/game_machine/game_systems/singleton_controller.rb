module GameMachine
  module GameSystems
    class SingletonController < Actor::Base

      attr_accessor :position, :entity
      def post_init(*args)
        @entity = args.first
        @position = Vector.new
        start
      end

      def start

      end

      def on_receive(message)
      end

      def update

      end

      def destroy(message)

      end

      def neighbors(type='player')
        GameMachine::GameSystems::EntityTracking.neighbors_from_grid(position.x,position.y,type)
      end

      def track(entity_type='npc')
        GameMachine::GameSystems::EntityTracking::GRID.set(entity.id,position.x,position.y,position.z,entity_type)
      end

    end
  end
end
