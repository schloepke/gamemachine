module GameMachine
  class UdpServerActor < GameActor

    def post_init(*args)
      @clients = {}
      @socket = nil
    end

    def preStart
      mgr = JavaLib::Udp.get(getContext.system).getManager
      inet = JavaLib::InetSocketAddress.new(Server.instance.config.udp.host, Server.instance.config.udp.port)
	    mgr.tell( JavaLib::UdpMessage.bind(getSelf,inet), getSelf)
    end

    def on_receive(message)
      if message.kind_of?(JavaLib::Udp::Bound)
        @socket = getSender
      elsif message.is_a?(ClientMessage)
        @socket.tell(client_to_udp_message(message), get_self)
        #@clients.delete(message.client_id.get_id)
      elsif message.kind_of?(JavaLib::Udp::Received)
        client_message = udp_to_client_message(message)
        GameActor.find(Settings.game_handler).send_message(client_message, :sender => get_self)
      elsif message == JavaLib::UdpMessage::unbind
        @socket.tell(message, get_self)
      elsif message.kind_of?(JavaLib::Udp::Unbound)
        get_context.stop(get_self)
      else
        unhandled(message)
      end
    end

    private

    def client_to_udp_message(client_message)
      sender = @clients.fetch(client_message.client_connection.get_id)
      byte_string = JavaLib::ByteString.from_array(client_message.data.to_byte_array)
      JavaLib::UdpMessage.send(byte_string, sender)
    end

    def udp_to_client_message(udp_message)
      @clients[udp_message.sender.to_s] = udp_message.sender
      client_connection = ClientConnection.new.set_id(udp_message.sender.to_s).set_gateway(self.class.name)
      client_message = ClientMessage.new(udp_message.data.to_array,client_connection)
    end

  end
end
