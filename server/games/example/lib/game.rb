require 'digest/md5'
require 'virtus'
require_relative 'models/vitals'
require_relative 'models/attack'
require_relative 'models/user'
require_relative 'models/combat_update'
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

      # Start our chatbot
      GameMachine::Actor::Builder.new(Chatbot,'global').start

      GameMachine::Actor::Builder.new(ZoneManager).start

      # Misc player management stuff is taken care of by the player manager.
      # This is game specific.
      GameMachine::Actor::Builder.new(PlayerManager).start

      # Our npc spawner. This is a fairly simple implementation that does not
      # have the best fault tolerance if the NpcGroup actors die for some reason.
      # That could be easily fixed by having them save the npc list in their
      # post_init, and read it on startup if it exists, recreating all the npc's.
      # A more advanced solution would query the region manager to see what region
      # it is responsible for, and load the npc list for that region.

      # Non aggressive npc's, these cannot be damaged
      #spawn_npcs('male',100,Npc)
      #spawn_npcs('viking',100,Npc)
      #spawn_npcs('golem',700,Npc)

      # Highly aggressive, players can damage these
      #spawn_npcs('worm',1000,AggressiveNpc)

      # Single combat controller. This could alternatively
      # run under a router to spread the load out (via with_router)
      GameMachine::Actor::Builder.new(CombatController).start

    end

    def spawn_npcs(type,count,klass)
      count.times.map {|i| "#{type}_#{i}"}.each_slice(20).each do |group|
        name = Digest::MD5.hexdigest(group.join(''))
        group.each do |npc_name|
          self.class.npcs[npc_name] = name
        end
        GameMachine::Actor::Builder.new(NpcGroup,group,klass).with_name(name).start
        sleep 0.05
      end
    end

  end
end
