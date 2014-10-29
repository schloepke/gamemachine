require 'socket'
require "uri"

module GameMachine
  module Clients
    class UdpClient

      attr_reader :host, :port, :socket
      def initialize(host,port)
        @host = host
        @port = port
        @socket = UDPSocket.new
        @socket.connect(host,port)
      end

      def send_message(message)
        socket.send(message.to_byte_array,host,port)
      end

      def receive_message
        socket.recvfrom(1024)[0]
      end

    end
  end
end
