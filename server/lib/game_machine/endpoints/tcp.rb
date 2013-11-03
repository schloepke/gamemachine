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

      def sanitize_address(name)
        name.gsub!('/','')
        name.gsub!('.','')
        name.gsub!(':','_')
        name
      end

      def on_receive(message)
        if message.is_a?(MessageLib::ClientMessage)
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
          handler_name = sanitize_address(message.remote_address.to_s)
          handler_ref = Actor::Builder.new(TcpHandler,handler_name).with_name(handler_name).start
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
