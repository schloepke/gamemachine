module Demo
  class NpcController < GameMachine::GameSystems::SingletonController

    # Start is our initializer, called at start and restart
    def start
      if @navmesh = GameMachine::Navigation::DetourNavmesh.find(1)
        @pathfinder = GameMachine::Navigation::DetourPath.new(@navmesh)
      else
        raise "Navmesh not set!"
      end
      position.set(entity.vector3.x, entity.vector3.y, entity.vector3.z)
      @target_position = GameMachine::Vector.new
      @last_move = Time.now.to_f
      @last_combat_update = Time.now.to_f
      @speed = 0.8
      unless saved_entity = GameMachine::ObjectDb.get(entity.id)
        GameMachine::ObjectDb.put(entity)
      end
      track
    end

    def find_path
      #path = @pathfinder.find_path(position.x,position.z,position.y,@target_position.x,@target_position.z,@target_position.y)
      path = @pathfinder.find_path(position.x,position.y,position.z,@target_position.x,@target_position.y, @target_position.z)
      if @pathfinder.error
        return nil
      else
        target_path = path.last
        puts path.inspect
        #puts "#{position.x},#{position.z},#{position.y} to #{@target_position.x},#{@target_position.z},#{@target_position.y} via #{target_path.inspect}"
        return GameMachine::Vector.new(target_path[0],target_path[1],target_path[2])
      end
    end

    def update

      # Get all nearby players
      players = neighbors('player')

      # Pick the first one as our target
      if players.size >= 1
        grid_value = players.get(0)
        @target_id = grid_value.id
        @target_position.set(grid_value.x,grid_value.y,grid_value.z)
        if path = find_path
          @target_position.set(path.x,path.y,path.z)
        else
        end
        @has_target = true
      else
        @has_target = false
      end

      # If we have a target we follow and attack if within range.
      # track updates the grid with our location. Only needs to be called
      # if we move.
      if @has_target
        move
        track
        attack
      end
    end

    def attack

      # Eligible to attack every 2 seconds
      combat_delta_time = Time.now.to_f - @last_combat_update.to_f
      return if combat_delta_time < 2

      # If target is within range, attack.  We choose the ability to use
      # and hand it off to the combat controller.
      distance_to_target = position.distance(@target_position)
      if distance_to_target <= 3
        attack_entity = Entity.new
        attack_entity.set_id(entity.id)
        attack_entity.set_attack(
          Attack.new.set_attacker(entity.id).
            set_target(@target_id).
            set_combat_ability_id(1)
        )
        CombatController.find.tell(attack_entity,get_self)
      end
      @last_combat_update = Time.now.to_f
    end

    def move
      @delta_time = Time.now.to_f - @last_move.to_f

      # Especially at startup, delta time can be too long adjust if
      # necessary
      if @delta_time >= 0.100
        @delta_time = 0.100
      end

      # Simple interpolation towards the target
      position.interpolate(@target_position,@speed * @delta_time)
      @last_move = Time.now.to_f
    end

    # The singleton manager sends update messages to all singleton
    # controllers at a configurable interval.  Default 100ms.
    def on_receive(message)
      update
    end

  end
end
