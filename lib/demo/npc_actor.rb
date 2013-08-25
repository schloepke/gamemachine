require_relative 'npc_behavior'
require 'benchmark'
module Demo
  class NpcActor < GameMachine::Actor::Base
    
    def post_init
      @scheduler = get_context.system.scheduler
      @dispatcher = get_context.system.dispatcher
      @npc_behaviors = {}
      @npc_index = {}
      @neighbors_index = {}
      #schedule_update
    end

    def on_receive(message)

      if message.is_a?(String)
        if message == 'update'
          @npc_behaviors.each do |npc_id,behavior|
            behavior.update
          end
        end
      elsif message.is_a?(Entity)
        if message.has_create_npc
          npc = message.create_npc.npc
          @npc_index[npc.id] = npc
          @npc_behaviors[npc.id] = NpcBehavior.new(npc,self)
        elsif message.has_neighbors
          if npc_behavior = @npc_behaviors.fetch(message.id,nil)
            npc_behavior.update(message.neighbors)
          else
            GameMachine.logger.error("NpcBehavior #{message.id} not found")
          end
        end
      end
      GameMachine.logger.debug("NpcActor got #{message}")
    end

    def schedule_update
      duration = GameMachine::JavaLib::Duration.create(100, java.util.concurrent.TimeUnit::MILLISECONDS)
      @scheduler.schedule(duration, duration, get_self, "update", @dispatcher, nil)
    end

  end
end

