module GameMachine
  module Physics
    class World < Actor::Base

      def self.create_world(x,y)
        bounds = Dyn4j::AxisAlignedBounds.new(x,y)
        @world = Dyn4j::World.new(bounds)
        vector = Dyn4j::Vector2.new(0,0)
        @world.set_gravity(vector)
        @world
      end

      def post_init(*args)
        @world = self.class.create_world(1000,1000)
        @players = {}
        @moves = {}
        @scheduler = get_context.system.scheduler
        @dispatcher = get_context.system.dispatcher
        #schedule_world_update
      end
      
      def update_players
        @moves.keys.each do |player_id|
          body = @players[player_id]
          move = @moves[player_id]
          puts "#{player_id} #{body.x} #{body.y} #{body.velocity}"
          if body.x >= move.first.floor and body.y >= move.last.floor
            body.set_velocity(0,0)
            puts "#{player_id} reached dest #{body.x} #{body.y}"
            @moves.delete(player_id)
          end
        end
        schedule_world_update
      end

      def on_receive(message)
        if message.is_a?(String)
          if message == 'update_world'
            time = Benchmark.realtime {@world.step;update_players}
            puts "TIME=#{time}"
          end
        elsif message.is_a?(Entity)
          player_id = message.player.id
          unless @players[player_id]
            player_body = Body.new
            @world.add_body(player_body.body)
            @players[player_id] = player_body
          end
          player_body = @players[player_id]

          if message.has_player_move
            target = message.player_move.target
            @moves[player_id] = [target.x,target.y]
            player_body.move_to(target.x,target.y) 
          end
        else
          unhandled(message)
        end
      end

      def schedule_world_update
        duration = JavaLib::Duration.create(10, java.util.concurrent.TimeUnit::MILLISECONDS)
        @scheduler.schedule_once(duration, get_self, "update_world", @dispatcher, nil)
        #@scheduler.schedule(duration, duration, get_self, "update_world", @dispatcher, nil)
      end

    end
  end
end
