module GameMachine
  module GameSystems
    class RemoteEcho < Actor::Base
      
      aspect %w(EchoTest)

      def on_receive(message)
        response = Helpers::GameMessage.new(message.player.id)
        response.client_message.add_entity(message)
        response.send_to_player
      end
    end
  end
end

