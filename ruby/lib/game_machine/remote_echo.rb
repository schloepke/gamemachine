module GameMachine
  class RemoteEcho < GameActor
    
    def self.components
      ['PlayerConnection']
    end

    def post_init(*args)
    end

    def on_receive(message)
      GameMachine.logger.debug("RemoteEcho client_id: #{message.get_client_connection.get_id} message: #{message}")
      ClientMessage.new(message,message.get_client_connection).send_to_client
    end
  end
end

