require_relative 'npc_controller'
require_relative 'combat_controller'
require_relative 'player_controller'

module Demo
  class Game
    include GameMachine::Commands

    attr_reader :game_root
    def initialize(game_root)
      @game_root = game_root
    end

    def start
      load_navmesh
      load_game_data
      GameMachine::Actor::Builder.new(CombatController).
        with_router(GameMachine::JavaLib::RoundRobinRouter,10).start
      500.times do |i|
        create_npc("#{GameMachine::Application.config.akka_port}_#{i}")
      end
    end

    def load_navmesh\
      meshfile = File.join(game_root,'data','meshes',"mesh1.bin")
      commands.navigation.load_navmesh(1,meshfile)
    end

    def create_npc(id)
      max = GameMachine::Settings.world_grid_size - 10

      x = 513.0#rand(max) + 1
      z = 529.0#rand(max) + 1
      x = rand(max) + 1
      z = rand(max) + 1
      y = 1.10
      entity = GameMachine::MessageLib::Entity.new
      entity.set_health(
        GameMachine::MessageLib::Health.new.set_health(100)
      )
      entity.set_id(id)
      entity.set_create_singleton(
        GameMachine::MessageLib::CreateSingleton.new.set_id(id).set_controller(Demo::NpcController.name)
      )
      entity.set_is_npc(GameMachine::MessageLib::IsNpc.new.set_enabled(true))
      entity.set_vector3(GameMachine::MessageLib::Vector3.new.set_x(x.to_f).set_y(y.to_f).set_z(z.to_f))

      GameMachine::GameSystems::SingletonManager.find.tell(entity)
    end

    def load_game_data
      GameMachine::GameData.load_from(
        File.join(game_root,'/data/game_data.yml')
      )
    end
  end
end
