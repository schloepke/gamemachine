require 'socket'
require "uri"

module GameMachine
  module Clients
    class TcpClient

      def initialize(server)
        @host = Settings.servers.send(server).tcp_host
        @port = Settings.servers.send(server).tcp_port
        @socket = TCPSocket.open(@host,@port)
      end

      def send_message(message)
        @socket.send(message,0)
        #@socket.send(String.from_java_bytes(message))
      end

      def receive_message
        @socket.recvfrom(1024)[0]
      end

    end
  end
end
