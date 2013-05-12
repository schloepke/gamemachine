module GameMachine
  module Server
    class HttpServer
      def self.run
        server = com.game_machine.http_server.HttpServer.new(8090)
        server.run
      end
    end
  end
end

