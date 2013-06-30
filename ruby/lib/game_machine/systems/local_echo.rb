module GameMachine
  module Systems
    class LocalEcho < Actor
      
      def on_receive(message)
        GameMachine.logger.debug("LocalEcho got #{message}")
        get_sender.tell(message,get_self)
      end
    end
  end
end
