module GameMachine
  class UdpServerActor < GameActor

    def preStart
      @clients = {}
      @socket = nil
      mgr = JavaLib::Udp.get(getContext.system).getManager
      inet = JavaLib::InetSocketAddress.new(Server.instance.config.udp.host, Server.instance.config.udp.port)
	    mgr.tell( JavaLib::UdpMessage.bind(getSelf,inet), getSelf)
    end

    def on_receive(message)
      if message.kind_of?(JavaLib::Udp::Bound)
        @socket = getSender
      elsif message.is_a?(ClientMessage)
        sender = @clients.fetch(message.client_id.get_id)
        @socket.tell(JavaLib::UdpMessage.send(JavaLib::ByteString.from_array(message.data.to_byte_array), sender), get_self)
        @clients.delete(message.client_id.get_id)
      elsif message.kind_of?(JavaLib::Udp::Received)
        @clients[message.sender.to_s] = message.sender
        client_id = ClientId.new.set_id(message.sender.to_s).set_gateway(self.class.name)
        client_message = ClientMessage.new(message.data.to_array,client_id)
        GameActor.find(Settings.game_handler).send_message(client_message, :sender => get_self)
      elsif message == JavaLib::UdpMessage::unbind
        @socket.tell(message, get_self)
      elsif message.kind_of?(JavaLib::Udp::Unbound)
        get_context.stop(get_self)
      else
        unhandled(message)
      end
    end

  end
end
