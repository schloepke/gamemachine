module GameMachine
  module Endpoints
    class Udt < JavaLib::UntypedActor
      class << self
        alias_method :apply, :new
        alias_method :create, :new
      end

      def preStart
        @clients = {}
      end

      def onReceive(message)
        Metric.increment(self.class.name,:on_receive)
        GameMachine.logger.debug("UdtServer onReceive #{message}")
        if message.is_a?(JavaLib::NetMessage)
          client_id = get_client_id(message)

          if message.protocol == JavaLib::NetMessage::DISCONNECTED
            if @clients[client_id]
              ClientObserver.notify_observers(client_id)
              @clients.delete(client_id)
            end
            return
          end

          @clients[client_id] = message
          client_message = create_client_message(message.bytes,client_id)
          Actor.find(Settings.game_handler).send_message(client_message, :sender => get_self)
        elsif message.is_a?(ClientMessage)
          if @clients[message.client_connection.id]
            JavaLib::UdtServer.get_udt_server.send_to_client(
              message.to_byte_array,
              @clients[message.client_connection.id].ctx
            )
          else
            GameMachine.logger.error "UDT outgoing message with invalid client_id"
          end
        else 
          unhandled(message)
        end
      end

      private

      def create_client_message(data,client_id)
        ClientMessage.parse_from(data).set_client_connection(
          ClientConnection.new.set_id(client_id).set_gateway(self.class.name)
        )
      end

      def get_client_id(netMessage)
        "#{netMessage.host}:#{netMessage.port}"
      end

    end
  end
end
