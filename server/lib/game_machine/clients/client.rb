require 'socket'
require "uri"

module GameMachine
  module Clients
    class Client

      def initialize(server)
        @host = Settings.servers.send(server).udp_host
        @port = Settings.servers.send(server).udp_port
        @socket = UDPSocket.new
        @socket.connect(@host,@port)
      end

      def send_message(message)
        @socket.send(String.from_java_bytes(message),@host,@port)
      end

      def receive_message
        @socket.recvfrom(1024)[0]
      end

      def self.http_post(path,data)
        uri = URI.parse("http://localhost:8080#{path}")
        response = Net::HTTP.post_form(uri, data)
        response.body
      end

      def self.connect_udt
        address = JavaLib::InetSocketAddress.new(Settings.servers.seed01.udt_host, Settings.servers.seed01.udt_port)
        s = JavaLib::SocketUDT.new(JavaLib::TypeUDT::DATAGRAM)
        s.setBlocking(true)
        s.connect(address)
        s
      end

      def self.send_udt(s,bytes)
        s.send(bytes)
        array = Java::byte[4096].new
        l = s.receive(array)
        JavaLib::Arrays.copy_of_range(array,0,l)
      end
    end
  end
end
