module GameMachine
  module Systems
    class RemoteEcho < Actor
      
      register_component 'EchoTest'

      def on_receive(message)
        GameMachine.logger.debug("RemoteEcho client_id: #{message.get_client_connection.get_id} message: #{message}")
        ClientMessage.new(message,message.client_connection).send_to_client
      end
    end
  end
end

