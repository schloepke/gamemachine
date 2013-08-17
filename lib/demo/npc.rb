module Demo
  class Npc < GameMachine::Actor::Base
    
    def post_init(*args)
    end

    def on_receive(message)
      GameMachine.logger.debug("Npc got #{message}")
      #get_sender.tell(message,get_self)
    end
  end
end
