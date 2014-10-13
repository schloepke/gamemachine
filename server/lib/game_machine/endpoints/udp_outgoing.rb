module GameMachine
  module Endpoints
    class UdpOutgoing < Actor::Base

      attr_reader :protocol, :client_connection, :client, :server, :player_id, :idle_timeout
      def post_init(*args)
        @client_connection = args[0]
        @client = args[1]
        @server = args[2]
        @player_id = args[3]
        @protocol = args[4]
        send_connected_message
        self.class.logger.debug "Player gateway created #{player_id}"

        @idle_timeout = AppConfig.instance.config.client.idle_timeout
        @last_activity = Time.now.to_i
        schedule_message('idle_check',10)
      end

      def send_connected_message
        client_message = create_client_message
        client_message.set_player_connected(MessageLib::PlayerConnected.new)
        send_to_client(client_message)
      end

      def create_client_message
        client_message = MessageLib::ClientMessage.new
        client_message.set_client_connection(client_connection)
      end

      def send_to_client(message)
        #self.class.logger.info("Sending #{message} out via #{protocol}")
        if protocol == 0
          bytes = message.to_byte_array
          server.sendToClient(
            client[:address],
            bytes,
            client[:ctx]
          )
        else
          client[:ctx].write(message)
          client[:ctx].flush
          JavaLib::UdpServerHandler.countOut.incrementAndGet
        end
      end

      def unregister_if_idle
        if (Time.now.to_i - @last_activity) > idle_timeout
          self.class.logger.info "Player #{player_id} timed out"
          client_message = create_client_message
          client_message.set_player(MessageLib::Player.new.set_id(player_id))
          GameMachine::Handlers::Request.unregister_client(client_message)

          self.class.logger.debug "Sending poison pill to self (#{player_id})"
          get_self.tell(JavaLib::PoisonPill.get_instance,nil)
          
        end
      end

      def on_receive(message)
        if message.is_a?(String)
          if message == 'idle_check'
            unregister_if_idle
          end
        else
          @last_activity = Time.now.to_i
          #GameMachine.logger.info "Sending message to player"
          client_message = create_client_message
          message.set_send_to_player(false)
          client_message.add_entity(message)
          send_to_client(client_message)
        end
      end
    end
  end
end
