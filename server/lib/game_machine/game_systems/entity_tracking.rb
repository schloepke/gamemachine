module GameMachine
  module GameSystems

    # @note Handles player location tracking and neighbor requests.
    #   The client must explicitly send a TrackEntit component to the server
    #   to be tracked, it doesn't happen automatically.
    #
    # @aspects TrackEntity
    # @aspects GetNeighbors
    class EntityTracking < Actor::Base
      include GameMachine::Commands

      aspect %w(TrackEntity)
      aspect %w(GetNeighbors)

      attr_reader :grid, :extra_params

      def post_init
        @entity_updates = []
        @grid = Grid.default_grid
        @paths = {}
        @width = grid.get_width
        @cell_count = grid.get_cell_count
        @extra_params = {}
      end

      def on_receive(message)
        if message.is_a?(MessageLib::Entity)
          if message.get_neighbors
            send_neighbors(message)
          end

          if message.track_entity
            set_entity_location(message)
          end
        else
          unhandled(message)
        end
      end

      private

      def set_entity_location(entity)
        vector = entity.vector3
        @grid.set(entity.id,vector.x,vector.y,vector.z,entity.entity_type)
        if entity.has_track_extra
          @extra_params[entity.id] = entity.track_extra
        end
      end

      def location_entity(grid_value)
        entity = MessageLib::Entity.new.set_id(grid_value.id)

        entity.set_vector3(
          MessageLib::Vector3.new.
          set_x(grid_value.x).
          set_y(grid_value.y).
          set_z(grid_value.z)
        )

        if @extra_params.has_key?(grid_value.id)
          track_extra = @extra_params.fetch(grid_value.id)
          entity.set_track_extra(track_extra)
        end
        entity
      end

      def send_neighbors(message)
        type = message.get_neighbors.neighbor_type
        x = message.get_neighbors.vector3.x
        y = message.get_neighbors.vector3.y

        # Either .net or java protobufs have a bug with 0 floats
        if x.nil?
          x = 0
        end
        if y.nil?
          y = 0
        end
        search_results = grid.neighbors(x,y,type)
       
        neighbors = search_results.map do |grid_value|
          location_entity(grid_value)
        end
 
        if neighbors.empty?
          return
        end

        if message.has_player
          send_neighbors_to_player(neighbors,message.player)
        end
      end

      def send_neighbors_to_player(neighbors,player)
        entity = MessageLib::Entity.new.set_neighbors(
          MessageLib::Neighbors.new.set_entity_list(neighbors)
        )
        entity.set_id(player.id)
        entity.set_player(player)
        entity.set_send_to_player(true)
        commands.player.send_message(entity,player.id)
      end

    end
  end
end
