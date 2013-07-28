module GameMachine
  module Clients
    class UdtClient

      def initialize(server)
        @host = Settings.servers.send(server).udt.host
        @port = Settings.servers.send(server).udt.port
      end

      def disconnect
        @socket.close
      end

      def connect
        address = JavaLib::InetSocketAddress.new(@host, @port)
        @socket = JavaLib::SocketUDT.new(JavaLib::TypeUDT::DATAGRAM)
        @socket.setBlocking(true)
        @socket.connect(address)
        @socket
      end

      def send_message(bytes)
        @socket.send(bytes)
      end

      def receive(timeout=0.100)
        array = Java::byte[4096].new
        res = @socket.receive(array)
        JavaLib::Arrays.copy_of_range(array,0,res)
      end
    end
  end
end

