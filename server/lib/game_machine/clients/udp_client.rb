require 'socket'
require "uri"

module GameMachine
  module Clients
    class UdpClient

      def initialize(server)
        @host = Settings.servers.send(server).udp_host
        @port = Settings.servers.send(server).udp_port
        @socket = UDPSocket.new
        @socket.connect(@host,@port)
      end

      def send_message(message)
        @socket.send(message,@host,@port)
      end

      def receive_message
        @socket.recvfrom(1024)[0]
      end

    end
  end
end
