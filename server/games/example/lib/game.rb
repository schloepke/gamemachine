require 'digest/md5'
require_relative 'models/vitals'
require_relative 'models/attack'
require_relative 'models/user'
require_relative 'models/combat_update'
require_relative 'models/player_command'
require_relative 'authentication_handler'
require_relative 'tracking_handler'
require_relative 'player_register'
require_relative 'example_controller'
require_relative 'chatbot'
require_relative 'combat_controller'
require_relative 'npc_movement'
require_relative 'npc'
require_relative 'aggressive_npc'
require_relative 'npc_group'
require_relative 'player_manager'
require_relative 'zone_manager'

module Example
  class Game
    include GameMachine::Commands

    attr_reader :game_root

    def self.npcs
      if @npcs
        @npcs
      else
        @npcs = java.util.concurrent.ConcurrentHashMap.new
      end
    end

    def initialize(game_root)
      @game_root = game_root
    end

    def start

      # Start your custom actors here.  In this example we show a simple method
      # for loading some static data and passing it to the actor.
      
      # Builder.new takes the classname of the actor,and any number of other arguments.
      # These extra arguments will be sent to the actor's post_init method when
      # it starts
      GameMachine::Actor::Builder.new(ExampleController).start

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

      # Start our chatbot.  No good reason for this, just shows off some more tech
      GameMachine::Actor::Builder.new(Chatbot,'global').start

      # Misc player management stuff is taken care of by the player manager.
      # This is game specific.
      GameMachine::Actor::Builder.new(PlayerManager).start

      # This actor is responsible for starting up everything associated with a zone.
      # It waits until the region manager tells it that it is responsible for a
      # particular zone.
      GameMachine::Actor::Builder.new(ZoneManager).start
    end


  end
end
