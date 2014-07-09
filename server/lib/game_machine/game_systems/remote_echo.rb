module GameMachine
  module GameSystems
    class RemoteEcho < Actor::Base
      include GameMachine::Commands
      aspect %w(EchoTest)

      def on_receive(message)
        GameMachine.logger.info "#{self.class.name} #{message}"
        commands.player.send_message(message,message.player.id)
      end
    end
  end
end

