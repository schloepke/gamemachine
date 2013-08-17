module GameMachine
  module GameSystems
    class PlayerTracking < Actor::Base

      aspect %w(TrackPlayer Player)
      aspect %w(GetNeighbors Player)

      def post_init(grid=default_grid)
        @grid = grid
        player_updates = Subscribe.new.set_topic('player_location_updates')
        MessageQueue.find.tell(player_updates,self)
      end

      def on_receive(message)

        # Ignore our own publishes
        if get_sender == get_self
          return
        end

        if message.is_a?(Entity)
          if message.track_player
            set_player_location(message.player)

            # Don't republish messages from other actors
            unless message.track_player.internal
              publish_player_location_update(message.player)
            end
          end

          if message.get_neighbors
            send_neighbors(message.player,message.get_neighbors.search_radius)
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

      def publish_player_location_update(player)
        publish = Publish.new.set_topic('player_location_updates').
          set_message(
            Entity.new.set_id('0').set_player(player).
            set_track_player(
              TrackPlayer.new.set_value(true).set_internal(true)
            )
          )
        MessageQueue.find.tell(publish,self)
      end

      def default_search_radius
        Settings.world_grid_cell_size
      end

      def default_grid
        Physics::Grid.new(Settings.world_grid_size,Settings.world_grid_cell_size)
      end

      def set_player_location(player)
        if values = @grid.get(player.id)
          @grid.remove(player.id)
        end
        @grid.set(player.id,player.x,player.y)
      end

      def send_neighbors(player,search_radius)
        players = neighbors_from_grid(player,search_radius)
        response = Helpers::GameMessage.new(player.id)
        response.neighbors(players)
        response.send_to_player
      end

      def player_from_neighbor(neighbor)
        Player.new.
          set_x(neighbor[2]).
          set_y(neighbor[3]).
          set_z(neighbor[4]).
          set_id(neighbor[0].to_s)
      end

      def neighbors_from_grid(player,search_radius)
        if search_radius.nil?
          search_radius = default_search_radius
        end
        @grid.neighbors(player.x,player.y,search_radius).map do |neighbor|
          player_from_neighbor(neighbor)
        end
      end

    end
  end
end
