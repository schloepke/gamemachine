module Example
  class NpcManager < GameMachine::Actor::Base

    attr_reader :npcs
    def post_init(*args)
      @npcs = args.first
      schedule_message('update',30)
    end

    def on_receive(message)

      if message == 'update'
        npcs.each {|npc| npc.tell('update',get_self)}
      end
    end
  end
end
