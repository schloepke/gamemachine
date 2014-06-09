require_relative 'npc_controller'
require_relative 'combat_controller'
require_relative 'player_controller'
require_relative 'example_controller'

module Demo
  class Game
    include GameMachine::Commands

    attr_reader :game_root
    def initialize(game_root)
      @game_root = game_root
    end

    def start
      GameMachine.logger.info "Starting #{ExampleController.name}"
      GameMachine::Actor::Builder.new(ExampleController).start
      load_game_data
    end

    def load_navmesh\
      meshfile = File.join(game_root,'data','meshes',"mesh1.bin")
      commands.navigation.load_navmesh(1,meshfile)
    end

    def load_game_data
      GameMachine::GameData.load_from(
        File.join(game_root,'/data/game_data.yml')
      )
    end
  end
end
