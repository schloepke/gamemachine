module Example
  class NpcGroup < GameMachine::Actor::Base
    include GameMachine::Commands

    attr :npcs
    def post_init(*args)
      @npcs = {}
      group = args.first
      group.each do |npc_id|
        npcs[npc_id] = Npc.new(npc_id)
      end
      schedule_message('update',50)
    end

    def on_receive(message)
      if message == 'update'
        npcs.each do |id,npc|
          npc.update
        end
      end
    end

  end
end
