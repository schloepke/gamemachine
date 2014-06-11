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

  end
end
