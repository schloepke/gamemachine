module GameMachine
  module Endpoints
    class MonoGateway < Actor::Base

      def post_init(*args)
        @clients = {}
        @sender = nil
        @socket = nil
        @mono_connected = false
        @last_mono_ping = nil
      end

      def preStart
        mgr = JavaLib::Udp.get(getContext.system).getManager
        inet = JavaLib::InetSocketAddress.new(
          Application.config.mono_gateway_host,
          Application.config.mono_gateway_port
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
        if message.has_destination
          if @sender
            byte_string = JavaLib::ByteString.from_array(message.to_byte_array)
            udp_message = JavaLib::UdpMessage.send(byte_string, @sender)
            @socket.tell(udp_message, get_self)
          else
            GameMachine.logger.error "#{self.class.name} cannot send outgoing message (no sender) "
          end
        else
          GameMachine.logger.error "#{self.class.name} destination missing (outgoing)"
        end
      rescue Exception => e
        GameMachine.logger.error "#{self.class.name} #{e.to_s}"
      end

      def handle_incoming(message)
        @sender = message.sender
        entity = MessageLib::Entity.parse_from(message.data.to_array)
        if entity.id == 'ping'
          unless @mono_connected
            GameMachine.logger.info "Mono server connected"
          end
          @mono_connected = true
          @last_mono_ping = Time.now.to_i
          echo(message)
        elsif entity.has_destination
          destination = entity.destination.sub(/^\//,'')
          destination = destination.gsub('/','::')
          entity.set_destination(nil)
          GameMachine.logger.info "#{self.class.name} incoming to #{destination}"
          Actor::Base.find(destination).tell(entity,get_self)
        else
          GameMachine.logger.error "#{self.class.name} destination missing (incoming)"
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
