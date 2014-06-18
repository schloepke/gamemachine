require 'digest/md5'
require_relative 'example_controller'
require_relative 'chatbot'
require_relative 'npc_movement'
require_relative 'npc'
require_relative 'aggressive_npc'
require_relative 'npc_group'

module Example
  class Game
    include GameMachine::Commands

    attr_reader :game_root
    def initialize(game_root)
      @game_root = game_root
    end

    def start

      # Start your custom actors here.  In this example we show a simple method
      # for loading some static data and passing it to the actor.
      
      # Builder.new takes the classname of the actor,and any number of other arguments.
      # These extra arguments will be sent to the actor's post_init method when
      # it starts
      game_data = load_game_data
      GameMachine::Actor::Builder.new(ExampleController,game_data).start

      # Creating the actor with a specific name.  Useful if you want to start up
      # multiple actors all using the same actor class.
      # GameMachine::Actor::Builder.new(ExampleController).with_name('my_example_actor').start


      # Using a router.
      # This starts up 10 copies of the actor with router in front of them that
      # distributes load using round robin
      # GameMachine::Actor::Builder.new(ExampleController).
      #   with_router(JavaLib::RoundRobinRouter,10)start

      # Using a distributed ring of actors.
      # This creates a dstributed actor ring that will hash messages to actors
      # using the id passed to ExampleActor.find_distributed(id).
      # GameMachine::Actor::Builder.new(ExampleController).
      #   distributed(10).start

      # Start our chatbot
      GameMachine::Actor::Builder.new(Chatbot,'global').start

      spawn_npcs('male',1000,Npc)
      spawn_npcs('viking',800,Npc)
      spawn_npcs('golem',1000,Npc)
      spawn_npcs('worm',800,AggressiveNpc)

    end

    def spawn_npcs(type,count,klass)
      count.times.map {|i| "#{type}_#{i}"}.each_slice(20).each do |group|
        name = Digest::MD5.hexdigest(group.join(''))
        GameMachine::Actor::Builder.new(NpcGroup,group,klass).with_name(name).start
        sleep 0.05
      end
    end

    def load_game_data
      # load_from is just a helper to load yaml data.
      GameMachine::GameData.load_from(
        File.join(game_root,'/data/game_data.yml')
      )
    end
  end
end
