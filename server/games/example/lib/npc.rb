module Example
  class Npc
    include GameMachine::Commands

    attr_accessor :position, :last_move, :speed, :id, :position_changed, :players, :target, :has_target
    def initialize(id)
      @id = id
      @position = GameMachine::Vector.new
      position.x = rand(1000) + 1
      position.y = rand(1000) + 1
      #position.z = 40
      @last_move = Time.now.to_f
      @speed = 0.4

      @last_ai = Time.now.to_i
      @step_count = 0
      @move_count = 0
      @position_changed = false
      @player_check_counter = 0
      @has_target = false
      @target = GameMachine::Vector.new

      get_players
      commands.grid.track(id,position.x,position.y,position.z)
      GameMachine.logger.info "Npc #{id} created"
    end

    def get_players
      @players = commands.grid.neighbors(position.x,position.y)
    end

    def check_players
      # If no players are in range, skip a couple of updates
      if players.empty?
        @player_check_counter += 1
        if @player_check_counter >= 4
          @player_check_counter = 0
          get_players
        end
      else
        get_players
      end
      players.size
    end

    def run_ai
      pick_random_target
    end

    def pick_random_target
      target.x = position.x + rand(-30..30)
      target.y = position.y + rand(-30..30)

      # Move one unit per second
      # tick = 50ms. one unit every 20 ticks
      distance = position.distance(target)
      if distance == 0
        return
      end

      @x_inc = (target.x - position.x) / distance
      @y_inc = (target.y - position.y) / distance
      if id == "Mob_400"
        #@move_start = Time.now.to_f
        #puts "Moving #{distance.inspect}"
      end
      @has_target = true
    end

    def update
      @position_changed = false
      run_ai unless has_target

      if has_target
        if @move_count >= 20
          move(target)
          @move_count = 0
        end
      end

      # Only update position if we moved
      if position_changed
        commands.grid.track(id,position.x.round(2),position.y.round(2),position.z)
        @position_changed = false
      end
      @move_count += 1
    end

    def move(target)
      delta_time = Time.now.to_f - last_move.to_f

      position.x += (@x_inc * delta_time)
      position.y += (@y_inc * delta_time)
      
      if position.distance(target) <= 1.0
        position.x = target.x
        position.y = target.y
        @has_target = false
        if id == "Mob_400"
          #puts "Move Finished #{Time.now.to_f - @move_start}"
        end
      end
      if id == "Mob_400"
        #puts "#{position.inspect} > #{target.inspect}"
      end
      @last_move = Time.now.to_f
      @position_changed = true
    end
  end
end
