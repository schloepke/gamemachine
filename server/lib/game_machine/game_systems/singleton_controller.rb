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


      # Returns a list of nearby tracked game objects
      def neighbors(type='player')
        GameMachine::GameSystems::EntityTracking.neighbors_from_grid(position.x,position.z,type)
      end

      # Call this to track a game object. Expects an entity with a TrackEntity
      # component attached
      def track(entity_type='npc')
        GameMachine::GameSystems::EntityTracking::GRID.set(entity.id,position.x,position.z,position.y,entity_type)
      end

    end
  end
end
