module GameMachine
  module Endpoints
    class MessageGateway < Actor::Base

      def post_init(*args)
        @clients = {}
        @sender = nil
        @socket = nil
      end

      def preStart
        mgr = JavaLib::Udp.get(getContext.system).getManager
        inet = JavaLib::InetSocketAddress.new(
          Application.config.message_gateway_host,
          Application.config.message_gateway_port
        )
        mgr.tell( JavaLib::UdpMessage.bind(getSelf,inet), getSelf)
      end

      def on_receive(message)
        if message.kind_of?(JavaLib::Udp::Bound)
          @socket = getSender
        elsif message.is_a?(MessageLib::Entity)
          handle_outgoing(message)
        elsif message.kind_of?(JavaLib::Udp::Received)
          handle_incoming(message)
        elsif message == JavaLib::UdpMessage::unbind
          @socket.tell(message, get_self)
        elsif message.kind_of?(JavaLib::Udp::Unbound)
          get_context.stop(get_self)
        else
          unhandled(message)
        end
      end

      private

      def handle_outgoing(message)
        if message.has_message_routing
          byte_string = JavaLib::ByteString.from_array(message.to_byte_array)
          udp_message = JavaLib::UdpMessage.send(byte_string, @sender)
          @socket.tell(udp_message, get_self)
        else
          GameMachine.logger.error "#{self.class.name} MessageRouting missing (outgoing)"
        end
      rescue Exception => e
        GameMachine.logger.error "#{self.class.name} #{e.to_s}"
      end

      def handle_incoming(message)
        @sender = message.sender
        entity = MessageLib::Entity.parse_from(message.data.to_array)
        if entity.id == 'ping'
          echo(message)
        elsif entity.has_message_routing
          destination = entity.message_routing.destination
          if destination.nil?
            GameMachine.logger.error "#{self.class.name} destination missing"
          else
            entity.set_message_routing(nil)
            Actor::Base.find(destination).tell(entity,get_self)
          end
        else
          GameMachine.logger.error "#{self.class.name} MessageRouting missing (incoming)"
        end
      rescue Exception => e
        GameMachine.logger.error "#{self.class.name} #{e.to_s}"
      end

      def echo(message)
        byte_string = JavaLib::ByteString.from_array(message.data.to_array)
        udp_message = JavaLib::UdpMessage.send(byte_string, message.sender)
        @socket.tell(udp_message,get_self)
      end

    end
  end
end
