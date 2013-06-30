module GameMachine
  module Systems
    class LocalEcho < Actor
      
      # Used in tests to see if actor got the message
      def self.echo(message)
      end

      def on_receive(message)
        GameMachine.logger.debug("LocalEcho got #{message}")
        self.class.echo(message)
        sender.send_message(message,:sender => self)
      end
    end
  end
end
