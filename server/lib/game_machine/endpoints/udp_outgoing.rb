module GameMachine
  module Endpoints
    class UdpOutgoing < Actor::Base

      attr_reader :client_connection, :client, :server, :player_id
      def post_init(*args)
        @client_connection = args[0]
        @client = args[1]
        @server = args[2]
        @player_id = args[3]
        send_connected_message
        GameMachine.logger.info "Player gateway created #{player_id}"
      end

      def send_connected_message
        client_message = create_client_message
        client_message.set_player_connected(MessageLib::PlayerConnected.new)
        send_to_client(client_message)
      end

      def create_client_message
        client_message = MessageLib::ClientMessage.new
        client_message.set_client_connection(client_connection)
      end

      def send_to_client(message)
        bytes = message.to_byte_array
        server.sendToClient(
          bytes,
          client[:host],
          client[:port],
          client[:ctx]
        )

      end

      def on_receive(message)
        #GameMachine.logger.info "Sending message to player"
        client_message = create_client_message
        message.set_send_to_player(false)
        client_message.add_entity(message)
        send_to_client(client_message)
      end
    end
  end
end
