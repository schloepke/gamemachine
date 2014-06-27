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

      # schedule_once was not yet in the base actor, needs to be abstracted
      # out to there
      unit = java.util.concurrent.TimeUnit::MILLISECONDS
      @duration = GameMachine::JavaLib::Duration.create(30, unit)
      @scheduler = get_context.system.scheduler
      @dispatcher = get_context.system.dispatcher
      schedule_once('update')
    end

    def schedule_once(message)
      @scheduler.schedule_once(@duration, get_self, message, @dispatcher, nil)
    end

    def on_receive(message)
      if message == 'update'
        npcs.each_value do |npc|
          npc.update
        end
        schedule_once('update')
      elsif message.is_a?(Models::CombatUpdate)
        if npc = npcs.fetch(message.target,nil)
          npc.update_combat(message)
        end
      end

    end

  end
end
