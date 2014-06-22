module Example
  class NpcGroup < GameMachine::Actor::Base
    include GameMachine::Commands

    attr :npcs
    def post_init(*args)
      @npcs = {}
      group = args.first
      klass = args.last
      group.each do |npc_id|
        npcs[npc_id] = klass.new(npc_id)
      end
      schedule_message('update',50)
    end

    def on_receive(message)
      if message == 'update'
        npcs.each do |id,npc|
          npc.update
        end
      elsif message.is_a?(Models::CombatUpdate)
        if npc = npcs.fetch(message.target,nil)
          npc.update_combat(message)
        end
      end

    end

  end
end
