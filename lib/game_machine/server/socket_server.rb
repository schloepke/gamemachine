require 'java'

module GameMachine
  module Server
    class SocketServer
      def self.run
        server = com.game_machine.socket_server.GameSocketServer.new(8080)
        server.run
      end
    end
  end
end
