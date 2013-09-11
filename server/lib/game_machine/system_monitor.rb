module GameMachine
  class SystemMonitor < Actor::Base

    def post_init(*args)
    end

    def preStart
      #getContext.system.eventStream.subscribe(getSelf, JavaLib::DeadLetter.java_class)
    end

    def on_receive(message)
      if message.is_a?(JavaLib::DeadLetter)
        #GameMachine.logger.debug("DeadLetter #{message.message}")
      else
        #GameMachine.logger.info("Unrecognized message #{message}")
      end
    end
  end
end
