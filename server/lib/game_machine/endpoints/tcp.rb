module GameMachine
  module Endpoints
    class Tcp < Actor::Base

      def post_init(*args)
        @clients = {}
        @mgr = nil
      end

      def preStart
        @mgr = JavaLib::Tcp.get(getContext.system).getManager
        inet = JavaLib::InetSocketAddress.new(
          Application.config.server.tcp_host,
          Application.config.server.tcp_port
        )
        @mgr.tell( JavaLib::TcpMessage.bind(getSelf,inet,100), getSelf)
      end

      def on_receive(message)
        if message.is_a?(ClientMessage)
          if handler_ref = @clients.fetch(message.client_connection.id,nil)
            handler_ref.tell(message)
          else
            GameMachine.logger.warn("No handler found for #{message.client_connection}")
          end
        elsif message.kind_of?(JavaLib::Tcp::Bound)
          @mgr.tell(message,get_self)
        elsif message.kind_of?(JavaLib::Tcp::CommandFailed)
          getContext.stop(get_self)
        elsif message.kind_of?(JavaLib::Tcp::Connected)
          @mgr.tell(message,get_self)
          handler_ref = Actor::Builder.new(TcpHandler).start
          @clients[message.remote_address.to_s] = handler_ref
          handler_ref.tell(message,get_sender)
          get_sender.tell(JavaLib::TcpMessage.register(handler_ref), get_self)
        else
          unhandled(message)
        end
      end

    end
  end
end
