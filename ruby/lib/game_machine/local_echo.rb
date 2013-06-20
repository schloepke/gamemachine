module GameMachine
  class LocalEcho < GameActor
    
    def post_init(*args)
    end

    def on_receive(message)
      GameMachine.logger.info("LocalEcho got #{message}")
      get_sender.tell(message,get_self)
    end
  end
end
