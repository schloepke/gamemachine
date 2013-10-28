module GameMachine
  module GameSystems

    # @abstract Subclass this to create a singleton controller
    class SingletonController < Actor::Base

      attr_accessor :position, :entity
      # @private
      def post_init(*args)
        @entity = args.first
        @position = Vector.new
        start
      end

      # called when the controller is started by the manager. Override this
      # in your subclass
      def start

      end

      # Override this in your subclass
      def on_receive(message)
      end


      def find_grid_value_by_id(id)
        GameMachine::GameSystems::EntityTracking::GRID.get(id)
      end

      def neighbors(type='player')
        GameMachine::GameSystems::EntityTracking.neighbors_from_grid(position.x,position.z,type)
      end

      def track(loc=position,entity_type='npc')
        GameMachine::GameSystems::EntityTracking::GRID.set(entity.id,loc.x,loc.z,loc.y,entity_type)
      end

    end
  end
end
