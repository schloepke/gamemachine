require_relative 'npc_behavior'

module Demo
  class NpcActor < GameMachine::Actor::Base
    
    def post_init
      @npc_behaviors = {}
      @npc_index = {}
      @neighbors_index = {}
      @scheduler = get_context.system.scheduler
      @dispatcher = get_context.system.dispatcher
      @update_running = false
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
          GameMachine.logger.info("Create npc")
          npc = message.create_npc.npc
          @npc_index[npc.id] = npc
          @npc_behaviors[npc.id] = NpcBehavior.new(npc,self)
          unless @update_running
            schedule_update
            @update_running = true
          end
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

