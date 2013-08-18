module GameMachine
  module GameSystems
    class EntityTracking < Actor::Base

      aspect %w(TrackEntity)
      aspect %w(GetNeighbors)

      attr_reader :grid

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
            entity = entity_from_message(message)

            if entity.nil?
              unhandled(message)
              return
            end

            set_entity_location(entity)

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
          GameMachine.logger.debug "EntityTracking Subscribed"
        else
          unhandled(message)
        end
      end

      private

      def entity_from_message(message)
        if message.has_player
          message.player
        elsif message.has_npc
          message.npc
        else
          nil
        end
      end

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
        @grid.set(entity)
      end

      def send_neighbors(message)
        search_radius = message.get_neighbors.search_radius
        x = message.get_neighbors.vector3.x
        y = message.get_neighbors.vector3.y
        neighbors = neighbors_from_grid(x,y,search_radius)

        if message.has_player
          player = message.player
          response = Helpers::GameMessage.new(message.player.id)
          response.neighbors(neighbors[:players],neighbors[:npcs])
          response.send_to_player
        else
          entity = Entity.new.set_neighbors(
            Neighbors.new.
            set_player_list(neighbors[:players]).
            set_npc_list(neighbors[:npcs])
          ).set_id('0')
          sender.tell(entity,self)
        end
      end

      def neighbors_from_grid(x,y,search_radius)
        if search_radius.nil?
          search_radius = default_search_radius
        end

        neighbors = {:players => [], :npcs => []}
        @grid.neighbors(x,y,search_radius).each do |neighbor|
          if neighbor[2].is_a?(Player)
            neighbors[:players] << neighbor[2]
          elsif neighbor[2].is_a?(Npc)
            neighbors[:npcs] << neighbor[2]
          end
        end
        neighbors
      end

    end
  end
end
