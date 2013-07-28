module GameMachine
  module GameSystems
    class PlayerTracking < Actor::Base

      aspect %w(TrackPlayer Player)
      aspect %w(GetNeighbors Player)

      def post_init(grid=Physics::Grid.new(10_000,25))
        @grid = grid
      end


      def on_receive(message)
        if message.track_player
          set_player_location(message.player)
        elsif message.get_neighbors
          players = get_neighbors(message.player)
          unless players.empty?
            response = Helpers::GameMessage.new(message.player.id)
            response.neighbors(players)
            response.send_to_player
          end
        end
      end

      private

      def set_player_location(player)
        if values = @grid.get(player.id)
          @grid.remove(player.id)
        end
        @grid.set(player.id,player.x,player.y)
      end

      def player_from_neighbor(neighbor)
        Player.new.
          set_x(neighbor[2]).
          set_y(neighbor[3]).
          set_id(neighbor[0].to_s)
      end

      def get_neighbors(player)
        @grid.neighbors(player.x,player.y,25).map do |neighbor|
          player_from_neighbor(neighbor)
        end
      end

    end
  end
end
