module GameMachine
  class PlayerGateway < Actor::Base

    attr_reader :client_connection, :client, :server, :player_id
    def post_init(*args)
      @client_connection = args[0]
      @client = args[1]
      @server = args[2]
      @player_id = args[3]
      GameMachine.logger.info "Player gateway created #{player_id}"
    end

    def on_receive(message)
      #GameMachine.logger.info "Sending message to player"
      client_message = MessageLib::ClientMessage.new
      client_message.set_client_connection(client_connection)
      message.set_send_to_player(false)
      client_message.add_entity(message)
      server.sendToClient(
        client_message.to_byte_array,
        client[:host],
        client[:port],
        client[:ctx]
      )
    end
  end
end
