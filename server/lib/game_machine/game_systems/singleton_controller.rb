module GameMachine
  module GameSystems

    # @abstract Subclass this to create a singleton controller
    class SingletonController < Actor::Base
      include GameMachine::Commands

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
        commands.grid.find_by_id(id)
      end

      def neighbors(type='player')
        commands.grid.neighbors(position.x,position.z,type)
      end

      def track(loc=position,entity_type='npc')
        commands.grid.track(entity.id,loc.x,loc.z,loc.y,entity_type)
      end

    end
  end
end
