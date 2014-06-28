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



      EXTRA = java.util.concurrent.ConcurrentHashMap.new
      attr_reader :grid, :extra_params, :aoe_grid

      def post_init
        if handler_klass = Application.config.entity_tracking_handler
         @tracking_handler = handler_klass.constantize.new
        end
        @entity_updates = []
        @grid = Grid.find_or_create('default')
        @aoe_grid = Grid.find_or_create('aoe')
        @paths = {}
        @width = grid.get_width
        @cell_count = grid.get_cell_count
        commands.misc.client_manager_register(self.class.name)
      end

      def on_receive(message)
        if message.is_a?(MessageLib::Entity)
          if message.get_neighbors
            send_neighbors(message)
          end

          # If a tracking handler is defined, it must return true to have
          # the player location saved
          if message.track_entity
            #GameMachine.logger.info "#{message.player.id} tracked"
            if @tracking_handler && message.entity_type == 'player'
              if @tracking_handler.verify(message)
                set_entity_location(message)
              end
            else
              set_entity_location(message)
            end
          end
        elsif message.is_a?(MessageLib::ClientManagerEvent)
          if message.event == 'disconnected'
            @grid.remove(message.player_id)
            @aoe_grid.remove(message.player_id)
            EXTRA.delete(message.player_id)
            GameMachine.logger.info "#{message.player_id} removed from grid"
          end
        else
          unhandled(message)
        end
      end

      private

      def set_entity_location(entity)
        vector = entity.vector3
        @grid.set(entity.id,vector.x,vector.y,vector.z,entity.entity_type)
        @aoe_grid.set(entity.id,vector.x,vector.y,vector.z,entity.entity_type)
        if entity.track_entity.has_track_extra
          track_extra = entity.track_entity.track_extra
          EXTRA[entity.id] = track_extra
        end
      end

      def location_entity(grid_value)
        entity = MessageLib::Entity.new.set_id(grid_value.id)

        entity.set_vector3(
          MessageLib::Vector3.new.
          set_x(grid_value.x.round(4)).
          set_y(grid_value.y.round(4)).
          set_z(grid_value.z.round(4))
        )

        if EXTRA.has_key?(grid_value.id)
          track_extra = EXTRA.fetch(grid_value.id)
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
        neighbors.each_slice(30) do |group|
          entity = MessageLib::Entity.new.set_neighbors(
            MessageLib::Neighbors.new.set_entity_list(group)
          )
          entity.set_id(player.id)
          entity.set_player(player)
          entity.set_send_to_player(true)
          commands.player.send_message(entity,player.id)
        end
      end

    end
  end
end
