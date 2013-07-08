module GameMachine
  module Systems
    class RemoteEcho < Actor
      
      aspect %w(EchoTest)

      def on_receive(message)
        GameMachine.logger.debug("RemoteEcho player_id: #{message.player.id} message: #{message}")
        response = Helpers::GameMessage.new(message.player.id)
        response.client_message.add_entity(message)
        response.send_to_player
      end
    end
  end
end

