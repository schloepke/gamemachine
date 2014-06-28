module Example
  class NpcMovement
    include GameMachine::Commands

    attr_accessor :updates_between_move
    attr_reader :current_target, :position, :updates_for_move,
      :position_changed, :has_target, :last_move, :id,
      :reached_target, :current_distance_to_target
    attr_accessor :speed_scale
    def initialize(id,position)
      @id = id
      @position = position
      @last_move = Time.now.to_f
      @position_changed = false
      @has_target = false
      @reached_target = false
      @updates_for_move = 0
      @updates_between_move = 15
      @speed_scale = 4
      @current_distance_to_target = 0
      commands.grid.track(id,position.x,position.y,position.z)
    end

    def update
      @position_changed = false

      if has_target && !reached_target
        if @updates_for_move >= updates_between_move
          move
          @updates_for_move = 0
        end
      else
        return
      end

      # Only update position if we moved
      if position_changed
        commands.grid.track(id,position.x,position.y,position.z)
        @position_changed = false
      end

      @updates_for_move += 1
    end

    def distance_to_target
      current_distance_to_target
    end

    def set_target(target)
      update_target(target)
      @last_move = Time.now.to_f
      @has_target = true
      @reached_target = false
    end

    def update_target(target)
      @current_distance_to_target = position.distance(target)
      @current_target = target

      if current_distance_to_target == 0
        @reached_target = true
        return
      end

      current_distance_to_target
    end

    def drop_target
      @current_target = nil
      @has_target = false
      @reached_target = false
      @current_distance_to_target = 0
    end

    def set_reached_target
      position.x = current_target.x
      position.y = current_target.y
      @reached_target = true
    end

    def move
      @current_distance_to_target =  position.distance(current_target)
      if current_distance_to_target == 0
        set_reached_target
        return
      end

      delta_time = Time.now.to_f - last_move.to_f

      # Save object creation by not using methods that return new vector
      x = current_target.x - position.x
      y = current_target.y - position.y
      dirx,diry = GameMachine::Vector.norm(x,y)
      position.x += dirx * speed_scale * delta_time
      position.y += diry * speed_scale * delta_time

      #dir = (current_target - position).normalize
      #position.x += dir.x * speed_scale * delta_time
      #position.y += dir.y * speed_scale * delta_time


      if position.distance(current_target) > current_distance_to_target
        set_reached_target
      end

      @last_move = Time.now.to_f
      @position_changed = true
    end
  end
end
