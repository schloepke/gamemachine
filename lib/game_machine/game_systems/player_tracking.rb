module GameMachine
  module GameSystems
    class PlayerTracking < Actor::Base

      aspect %w(TrackPlayer Player)
      aspect %w(GetNeighbors Player)

      def post_init(*args)
        @grid = Physics::Grid.new(10_000,25)
      end

      def set_player_location(player)
        if values = @grid.get(player.id)
          @grid.remove(player.id)
        end
        @grid.set(player.id,player.x,player.y)
      end

      def get_neighbors(player)
        @grid.neighbors(player.x,player.y,25)
      end

      def on_receive(message)
        if message.track_player
          set_player_location(message.player)
        elsif message.get_neighbors
          get_neigbors(message.player)
        end
      end
    end
  end
end
