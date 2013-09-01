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
        if @track_entity
          @track_entity.vector3.set_x(position.x).
            set_y(position.y)
        else
          @track_entity = Entity.new.set_track_entity(
            TrackEntity.new.set_value(true)
          ).set_id(entity.id)
          @track_entity.set_entity_type(entity_type)
          @track_entity.set_vector3(
            Vector3.new.set_x(position.x).set_y(position.y)
          )
        end

        unless @actor_ref
          @actor_ref = EntityTracking.find.actor
        end
        @actor_ref.tell(@track_entity)
      end

    end
  end
end
