module GameMachine
  class UdpServerActor < GameActor

    def preStart
      @clients = {}
      @socket = nil
      mgr = Udp.get(getContext.system).getManager
	    mgr.tell(UdpMessage.bind(getSelf, InetSocketAddress.new("localhost", 8100)), getSelf)
    end

    def on_receive(message)
      if message.kind_of?(Udp::Bound)
        @socket = getSender
      elsif message.is_a?(ClientMessage)
        sender = @clients[message.client_id.get_id]
        @socket.tell(UdpMessage.send(ByteString.from_array(message.data.to_byte_array), sender), get_self)
        @clients.delete(message.client_id.get_id)
      elsif message.kind_of?(Udp::Received)
        @clients[message.sender.to_s] = message.sender
        client_id = ClientId.new.set_id(message.sender.to_s).set_gateway(self.class.name)
        client_message = ClientMessage.new(message.data.to_array,client_id)
        GameActor.find(Settings.game_handler).send_message(client_message, :sender => get_self)
      elsif message == UdpMessage::unbind
        socket.tell(message, get_self)
      elsif message.kind_of?(Udp::Unbound)
        get_context.stop(get_self)
      else
        unhandled(message)
      end
    end

  end
end
