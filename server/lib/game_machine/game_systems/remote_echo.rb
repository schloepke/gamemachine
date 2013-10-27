module GameMachine
  module GameSystems
    class RemoteEcho < Actor::Base
      
      aspect %w(EchoTest)

      def on_receive(message)
        message.set_send_to_player(true)
        MessageLib::PlayerGateway.find.tell(message)
      end
    end
  end
end

