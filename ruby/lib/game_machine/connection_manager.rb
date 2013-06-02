module GameMachine
  class ConnectionManager < ActorBase

    def self.components
      ['ClientConnection']
    end

    def initialize
      @authorized_clients = {}
    end
    
    def on_receive(message)
      if message[:authorized]
        @authorized_clients[message[:client_id]] = true
      elsif message[:disconnected]
        @authorized_clients.delete(message[:client_id])
      end
    end
  end
end
