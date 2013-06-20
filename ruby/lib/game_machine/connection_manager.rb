module GameMachine
  class ConnectionManager < GameActor

    def self.components
      ['PlayerConnection']
    end

    def initialize
      @authorized_clients = {}
    end

    def on_receive(message)
      GameMachine.logger.info("ConnectionManager got #{message}")
    end
  end
end
