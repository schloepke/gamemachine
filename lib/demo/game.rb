require_relative 'npc_controller'
module Demo
  class Game

    def start
      load_game_data
      GameMachine::Actor::Builder.new(CombatController).
        with_router(GameMachine::JavaLib::RoundRobinRouter,10).start
      2000.times do |i|
        create_npc("#{GameMachine::Application.config.akka_port}_#{i}")
      end
    end

    def create_npc(id)
      max = GameMachine::Settings.world_grid_size - 10

      x = rand(max) + 1
      y = rand(max) + 1
      z = 1.10
      entity = Entity.new
      entity.set_health(
        Health.new.set_health(100)
      )
      entity.set_id(id)
      entity.set_create_singleton(
        CreateSingleton.new.set_id(id).set_controller(Demo::NpcController.name)
      )
      entity.set_is_npc(IsNpc.new.set_enabled(true))
      entity.set_vector3(Vector3.new.set_x(x.to_f).set_y(y.to_f).set_z(z.to_f))

      GameMachine::GameSystems::SingletonManager.find.tell(entity)
    end

    def load_game_data
      GameMachine::GameSystems::GameData.load_from(
        File.join(GameMachine.app_root,'lib/demo/game_data.yml')
      )
    end
  end
end
