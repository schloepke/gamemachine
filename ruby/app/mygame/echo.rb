module Mygame
  class Echo < GameMachine::GameActor
    
    def post_init(*args)
    end

    def on_receive(message)
      GameMachine.logger.debug("Echo got #{message}")
      get_sender.tell(message,get_self)
    end
  end
end
