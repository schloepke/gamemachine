module GameMachine
  class UdtServerActor < JavaLib::UntypedActor

    class << self
      alias_method :apply, :new
      alias_method :create, :new
    end

    def preStart
      @clients = {}
    end

    def onReceive(message)
      GameMachine.logger.debug("UdtServer onReceive #{message}")
      if message.is_a?(NetMessage)
        client_id_str = get_client_id(net_message)
        client_id = ClientId.new.set_id(client_id_str).set_gateway(self.class.name)
        @clients[client_id_str] = net_message
        client_message = ClientMessage.new(net_message.bytes, client_id)
        GameActor.find(Settings.game_handler).send_message(client_message, :sender => get_self)
      elsif message.is_a?(ClientMessage)
        UdtServer.get_udt_server.send_to_client(
          message.data.to_byte_array,
          @clients[message.client_id.get_id].ctx
        )
        @clients.delete(message.client_id.get_id)
      else 
        unhandled(message)
      end
    end

    private

    def get_client_id(netMessage)
      "#{netMessage.host}:#{netMessage.port}"
    end

  end
end
