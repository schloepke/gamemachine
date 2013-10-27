module GameMachine
  module Endpoints
    class ActorUdp < Actor::Base

      def post_init(*args)
        @clients = {}
        @socket = nil
      end

      def preStart
        mgr = JavaLib::Udp.get(getContext.system).getManager
        inet = JavaLib::InetSocketAddress.new('127.0.0.1',4000)
        mgr.tell( JavaLib::UdpMessage.bind(getSelf,inet), getSelf)
      end

      def on_receive(message)
        if message.is_a?(MessageLib::Entity)
          handle_outgoing(message)
        elsif message.kind_of?(JavaLib::Udp::Bound)
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

      def reply_to_sender(message,sender)
        byte_string = JavaLib::ByteString.from_array(message.to_byte_array)
        udp_message = JavaLib::UdpMessage.send(byte_string, sender)
        @socket.tell(udp_message, get_self)
      rescue Exception => e
        GameMachine.logger.error "#{self.class.name} #{e.to_s}"
      end

      def handle_incoming(message)
        entity = MessageLib::Entity.parse_from(message.data.to_array)
        if entity.has_message_envelope
          send_tell(entity)
        elsif entity.has_rpc
          method = entity.rpc.method
          arguments = entity.rpc.arguments
          result = run_method(method,arguments)
          if entity.rpc.return_value
            reply_to_sender(result,message.sender)
          end
        end
      rescue Exception => e
        GameMachine.logger.error "#{self.class.name} #{e.message} #{e.to_s}"
      end

      def run_method(method,arguments)
        if method == 'track'
          id = arguments.get(0)
          x = arguments.get(1).to_i
          y = arguments.get(2).to_i
          z = arguments.get(3).to_i
          entity_type = arguments.get(4)
          GameMachine::GameSystems::EntityTracking::GRID.set(
            id,x,z,y,entity_type
          )
        elsif method == 'neighbors'
          x = arguments.get(0).to_f
          z = arguments.get(1).to_f
          type = arguments.get(2)
          GameMachine::GameSystems::EntityTracking.
            neighbors_to_entity(x,z,type)
        end
      end

      def send_tell(entity)
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
    end
  end
end
