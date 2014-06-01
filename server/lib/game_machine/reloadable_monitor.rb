module GameMachine
  class ReloadableMonitor < Actor::Base

    def post_init(*args)
      @scheduler = get_context.system.scheduler
      @dispatcher = get_context.system.dispatcher
      update_interval = 2000
      schedule_update(update_interval)
    end

    def update_reloadable
      GameMachine::Actor::Reloadable.update_paths
    end

    def on_receive(message)
      if message == 'update_reloadable'
        update_reloadable
      end
    end

    def schedule_update(update_interval)
      duration = JavaLib::Duration.create(update_interval, java.util.concurrent.TimeUnit::MILLISECONDS)
      @scheduler.schedule(duration, duration, get_self, "update_reloadable", @dispatcher, nil)
    end
  end
end
