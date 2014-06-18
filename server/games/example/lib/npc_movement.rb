module Example
  class NpcMovement
    include GameMachine::Commands

    attr_reader :current_target, :position, :move_x, :move_y, :updates_for_move,
      :position_changed, :has_target, :updates_between_move, :last_move, :id,
      :reached_target
    def initialize(id,position)
      @id = id
      @position = position
      @last_move = Time.now.to_f
      @position_changed = false
      @has_target = false
      @reached_target = false
      @updates_for_move = 0
      @updates_between_move = 20
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
        commands.grid.track(id,position.x.round(2),position.y.round(2),position.z)
        @position_changed = false
      end

      @updates_for_move += 1
    end

    def distance_to_target
      current_target ? position.distance(current_target) : 0
    end

    def set_target(target)
      update_target(target)
      @last_move = Time.now.to_f
      @has_target = true
      @reached_target = false
    end

    def update_target(target)
      distance = position.distance(target)
      if distance == 0
        return false
      end
      @current_target = target
      @move_x = (current_target.x - position.x) / distance
      @move_y = (current_target.y - position.y) / distance
    end

    def drop_target
      @current_target = nil
      @move_x = nil
      @move_y = nil
      @has_target = false
      @reached_target = false
    end

    def move
      delta_time = Time.now.to_f - last_move.to_f

      position.x += (move_x * delta_time)
      position.y += (move_y * delta_time)
      
      if position.distance(current_target) <= 1.0
        position.x = current_target.x
        position.y = current_target.y
        @reached_target = true
      end

      #if id.match(/worm/)
      #  puts "#{id}: x: #{move_x} y: #{move_y} #{position.inspect} --> #{current_target.inspect}   distance: #{position.distance(current_target)} time: #{delta_time}"
      #end
      @last_move = Time.now.to_f
      @position_changed = true
    end
  end
end
