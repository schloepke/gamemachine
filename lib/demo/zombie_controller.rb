module Demo
  class ZombieController < GameMachine::GameSystems::SingletonController

    def start
      position.set(entity.vector3.x, entity.vector3.y, entity.vector3.z)
      @target_position = GameMachine::Vector.new
      @last_move = Time.now.to_f
      @last_target_time = Time.now.to_f
      @last_combat_update = Time.now.to_f
      @speed = 0.8
      unless saved_entity = GameMachine::ObjectDb.get(entity.id)
        GameMachine::ObjectDb.put(entity)
      end
      track
    end

    def update
      players = neighbors('player')
      if players.size >= 1
        grid_value = players.get(0)
        @target_id = grid_value.id
        @target_position.set(grid_value.x,grid_value.y,grid_value.z)
        @has_target = true
        @last_target_time = Time.now.to_f
      else
        @has_target = false
      end

      if @has_target
        move
        track
        @combat_delta_time = Time.now.to_f - @last_combat_update.to_f
        if @combat_delta_time >= 2
          attack
          @last_combat_update = Time.now.to_f
        end
      end
    end

    def attack
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
    end

    def move
      @delta_time = Time.now.to_f - @last_move.to_f
      if @delta_time >= 0.100
        @delta_time = 0.100
      end
      position.interpolate(@target_position,@speed * @delta_time)
      @last_move = Time.now.to_f
    end

    def on_receive(message)
      update
    end

  end
end
