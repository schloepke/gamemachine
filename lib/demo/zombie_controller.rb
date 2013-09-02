module Demo
  class ZombieController < GameMachine::GameSystems::SingletonController

    def start
      position.set(entity.vector3.x, entity.vector3.y, entity.vector3.z)
      @target_position = GameMachine::Vector.new
      @last_update = Time.now.to_f
      @speed = 0.8
      unless saved_entity = GameMachine::ObjectDb.get(entity.id)
        GameMachine::ObjectDb.put(entity)
      end
    end

    def update
      players = neighbors('player')
      if players.size >= 1
        grid_value = players.get(0)
        @target_position.set(grid_value.x,grid_value.y,grid_value.z)
        @has_target = true
      end

      move if @has_target
      track
    end

    def move
      @delta_time = Time.now.to_f - @last_update.to_f
      position.interpolate(@target_position,@speed * @delta_time)
      @last_update = Time.now.to_f
    end

    def on_receive(message)
      update
    end

  end
end
