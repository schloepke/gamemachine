module Demo
  class ZombieController < GameMachine::GameSystems::SingletonController

    def start
      @position = GameMachine::Vector.new(
        @npc.vector3.x,
        @npc.vector3.y,
        @npc.vector3.z
      )
      @target_position = GameMachine::Vector.new
      @last_update = Time.now.to_f
      @speed = 0.8
    end

    def update
      if neighbors('player').size >= 1
        grid_value = neighbors.get(0)
        @target_position.set(grid_value.x,grid_value.y,grid_value.z)
        @has_target = true
      end

      move if @has_target
      track
    end

    def move
      @delta_time = Time.now.to_f - @last_update.to_f
      @position.interpolate(@target_position,@speed * @delta_time)
      @last_update = Time.now.to_f
    end

    def destroy(message)

    end

    def on_receive(message)

    end

  end
end
