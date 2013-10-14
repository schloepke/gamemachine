module GameMachine
  module Endpoints
    class ActorUdp < Actor::Base

      def post_init(*args)
        @socket = nil
      end

      def preStart
        mgr = JavaLib::Udp.get(getContext.system).getManager
        inet = JavaLib::InetSocketAddress.new('127.0.0.1',4000)
        mgr.tell( JavaLib::UdpMessage.bind(getSelf,inet), getSelf)
      end

      def on_receive(message)
        if message.kind_of?(JavaLib::Udp::Bound)
          @socket = getSender
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

      def handle_incoming(message)
          entity = Entity.parse_from(message.data.to_array)
          if entity.has_message_envelope
            me = entity.message_envelope
            entity.set_message_envelope(nil)
            case me.type
            when 'l'
              Actor::Base.find(me.name).tell(entity)
            when 'r'
              Actor::Base.find_remote(me.server,me.name).tell(entity)
            when 'dl'
              Actor::Base.find_distributed_local(me.id,me.name).tell(entity)
            when 'd'
              Actor::Base.find_distributed(me.id,me.name).tell(entity)
            end
          end
      rescue Exception => e
        GameMachine.logger.error "#{self.class.name} #{e.to_s}"
      end
    end
  end
end
