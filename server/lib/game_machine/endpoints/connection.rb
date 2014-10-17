module GameMachine
  module Endpoints
    class Connection

      attr_reader :client_connection, :client, :server, :player_id, :protocol

      def initialize(client_connection,client,server,player_id,protocol)
        @client_connection = client_connection
        @client = client
        @server = server
        @player_id = player_id
        @protocol = protocol
      end

      def send_to_client(message)
        #self.class.logger.info("Sending #{message} out via #{protocol}")
        if protocol == 0
          bytes = message.to_byte_array
          server.sendToClient(
            client[:address],
            bytes,
            client[:ctx]
          )
        else
          client[:ctx].write(message)
          client[:ctx].flush
          JavaLib::UdpServerHandler.countOut.incrementAndGet
        end
      end
      
    end
  end
end