module GameMachine
  module Protocols
    class Udt < JavaLib::UntypedActor
      class << self
        alias_method :apply, :new
        alias_method :create, :new
      end

      def preStart
        @clients = {}
      end

      def onReceive(message)
        GameMachine.logger.debug("UdtServer onReceive #{message}")
        if message.is_a?(JavaLib::NetMessage)
          client_id_str = get_client_id(message)
          client_connection = ClientConnection.new.set_id(client_id_str).set_gateway(self.class.name)
          @clients[client_id_str] = message
          client_message = ClientMessage.new(message.bytes, client_connection)
          Actor.find(Settings.game_handler).send_message(client_message, :sender => get_self)
        elsif message.is_a?(ClientMessage)
          JavaLib::UdtServer.get_udt_server.send_to_client(
            message.data.to_byte_array,
            @clients[message.client_connection.get_id].ctx
          )
          @clients.delete(message.client_connection.get_id)
        else 
          unhandled(message)
        end
      end

      private

      def get_client_id(netMessage)
        "#{netMessage.host}:#{netMessage.port}"
      end

    end
  end
end
