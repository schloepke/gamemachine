module GameMachine
  class SystemMonitor < GameActor

    def post_init(*args)
    end

    def preStart
    end

    def on_receive(message)
      GameMachine.logger.error("SystemMonitor #{message.inspect}")
    end
  end
end
