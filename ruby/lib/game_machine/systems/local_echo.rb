module GameMachine
  module Systems
    class LocalEcho < Actor
      
      def on_receive(message)
        GameMachine.logger.debug("LocalEcho got #{message}")
        sender.send_message(message,:sender => self)
      end
    end
  end
end
