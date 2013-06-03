module GameMachine
  class ConnectionManager < ActorBase

    def self.components
      ['PlayerConnection']
    end

    def initialize
      @authorized_clients = {}
    end

    def on_receive(game_message)
      
    end
  end
end
