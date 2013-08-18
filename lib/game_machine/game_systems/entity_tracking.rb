module GameMachine
  module GameSystems
    class EntityTracking < Actor::Base

      aspect %w(TrackEntity Transform)
      aspect %w(GetNeighbors)

      def post_init(grid=default_grid)
        @grid = grid
        entity_updates = Subscribe.new.set_topic('entity_location_updates')
        MessageQueue.find.tell(entity_updates,self)
      end

      def on_receive(message)

        # Ignore our own publishes
        if get_sender == get_self
          return
        end

        if message.is_a?(Entity)
          if message.track_entity
            set_entity_location(message)

            # Don't republish messages from other actors
            unless message.track_entity.internal
              publish_entity_location_update(message)
            end
          end

          if message.get_neighbors
            send_neighbors(message)
          else
            unhandled(message)
          end

        elsif message.is_a?(JavaLib::DistributedPubSubMediator::SubscribeAck)
          GameMachine.logger.debug "PlayerTracking Subscribed"
        else
          unhandled(message)
        end
      end

      private

      def publish_entity_location_update(entity)
        publish_entity = entity.clone
        publish_entity.track_entity.set_internal(true)
        publish = Publish.new.
          set_topic('entity_location_updates').
          set_message(publish_entity)
        MessageQueue.find.tell(publish,self)
      end

      def default_search_radius
        Settings.world_grid_cell_size
      end

      def default_grid
        Physics::Grid.new(Settings.world_grid_size,Settings.world_grid_cell_size)
      end

      def set_entity_location(entity)
        if values = @grid.get(entity.id)
          @grid.remove(entity.id)
        end
        x = entity.transform.vector3.x
        y = entity.transform.vector3.y
        @grid.set(entity.id,x,y)
      end

      def send_neighbors(message)
        player = message.player
        search_radius = message.get_neighbors.search_radius
        entities = neighbors_from_grid(message.transform,search_radius)
        response = Helpers::GameMessage.new(message.player.id)
        response.neighbors(entities)
        response.send_to_player
      end

      def player_from_neighbor(neighbor)
        Player.new.
          set_x(neighbor[2]).
          set_y(neighbor[3]).
          set_z(neighbor[4]).
          set_id(neighbor[0].to_s)
      end

      def neighbors_from_grid(transform,search_radius)
        if search_radius.nil?
          search_radius = default_search_radius
        end
        x = transform.vector3.x
        y = transform.vector3.y
        @grid.neighbors(x,y,search_radius).map do |neighbor|
          player_from_neighbor(neighbor)
        end
      end

    end
  end
end
