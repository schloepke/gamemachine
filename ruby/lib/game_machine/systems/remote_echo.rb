module GameMachine
  module Systems
    class RemoteEcho < Actor
      
      register_component 'EchoTest'

      def on_receive(message)
        GameMachine.logger.debug("RemoteEcho client_id: #{message.get_client_connection.get_id} message: #{message}")
        client_message = ClientMessage.new
        client_message.add_entity(message)
        client_message.set_client_connection(message.client_connection)
        client_message.send_to_client
      end
    end
  end
end

