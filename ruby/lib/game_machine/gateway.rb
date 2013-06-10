module GameMachine
  class Gateway < UntypedActor

    class << self
      alias_method :apply, :new
      alias_method :create, :new

      def send_to_client(clientId, bytes)
        gatewayMessage = GatewayMessage.new(bytes, clientId)
        ActorUtil.getSelectionByName(self.name).tell(gatewayMessage, nil)
      end

    end

    def initialize
      @clients = {}
    end

    def onReceive(message)
      if message.is_a?(NetMessage)
        handle_net_message(message)
      elsif message.is_a?(GatewayMessage)
        handle_gateway_message(message)
      else 
        unhandled(message)
      end
    end

    private

    def add_client(client_id,net_message)
      @clients[client_id] = net_message
    end

    def get_client(client_id)
      @clients.fetch(client_id)
    end

    def handle_net_message(net_message)
      client_id = get_client_id(net_message)

      gatewayMessage = GatewayMessage.new(net_message.bytes, client_id)
      add_client(client_id,net_message)
      ActorUtil.getSelectionByName(GameMachineLoader.getGameHandler).tell(
        gatewayMessage,
        self.getSelf
      )
      GameMachine.logger.debug("Inbound NetMessage message: #{net_message.bytes}")
    end

    def handle_gateway_message(gateway_message)
      GameMachine.logger.debug("Outbound message: {}")
      net_message = get_client(gateway_message.get_client_id)

      if net_message.protocol == NetMessage::UDP
        UdpServer.get_udp_server.send_to_client(
          gateway_message.get_bytes,
          net_message.host,
          net_message.port,
          net_message.ctx
        )
      elsif net_message.protocol == NetMessage::UDT
        UdtServer.get_udt_server.send_to_client(
          gateway_message.get_bytes,
          net_message.ctx
        )
      else
        unhandled(message)
      end
    end

    def get_client_id(netMessage)
      "#{netMessage.host}:#{netMessage.port}"
    end

  end
end
