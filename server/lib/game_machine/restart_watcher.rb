module GameMachine
  class RestartWatcher < Actor::Base

    def post_init(*args)
      schedule_message('check_restart',1000)
    end

    def on_receive(message)
      if message == 'check_restart'
        if Console::Server.restart?
          Console::Server.exit!
        end
      end
    end

    def schedule_message(message,update_interval)
      duration = GameMachine::JavaLib::Duration.create(
        update_interval, java.util.concurrent.TimeUnit::MILLISECONDS
      )
      scheduler = get_context.system.scheduler
      dispatcher = get_context.system.dispatcher
      scheduler.schedule(duration, duration, get_self, message, dispatcher, nil)
    end
  end
end
