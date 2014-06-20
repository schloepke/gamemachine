module GameMachine
  module Endpoints
    class UdpIncoming < Actor::Base

      def self.clients
        if @clients
          @clients
        else
          @clients = java.util.concurrent.ConcurrentHashMap.new
        end
      end

      attr_reader :game_handler, :server, :clients
      def post_init(*args)
        @game_handler = Actor::Base.find(Application.config.game_handler)
        @server = JavaLib::UdpServer.getUdpServer
        @server_handler = @server.getHandler
        @auth_handler = Handlers::Authentication.new
      end

      def on_receive(message)
        if message.is_a?(JavaLib::NetMessage)
          handle_incoming(message)
        end
      end

      def handle_incoming(message)
        client_id = "#{message.host}:#{message.port}"
        client_connection = create_client_connection(client_id)
        client_message = create_client_message(message.bytes,client_connection)

        # Ensure we kill the player gateway actor on logout or on new connection
        if client_message.has_player_logout || client_message.has_player_connect

          # Ensure valid authtoken before doing anything
          unless @auth_handler.valid_authtoken?(client_message.player)
            GameaMachine.logger.info "Unauthenticated client attempting to login/logout"
            return
          end

          destroy_child(client_message.player.id)
          self.class.clients.delete(client_message.player.id)
          
          if client_message.has_player_connect
            unless self.class.clients.has_key?(client_message.player.id)
              client = {
                :host => message.host,
                :port => message.port,
                :ctx => message.ctx,
                :client_connection => client_connection
              }
              GameMachine.logger.info "#{client.inspect}"
              self.class.clients[client_message.player.id] = client
              create_child(client_connection,client,@server,client_message.player.id)
            end
          end
        end

        game_handler.send_message(
          client_message, :sender => get_self
        )
      rescue Exception => e
        GameMachine.logger.error "#{self.class.name} #{e.to_s}"
      end

      def create_child(client_connection,client,server,player_id)
        builder = Actor::Builder.new(Endpoints::UdpOutgoing,client_connection,client,server,player_id)
        builder.with_name(player_id).start
      end

      def destroy_child(player_id)
        Actor::Base.find(player_id).tell(JavaLib::PoisonPill.get_instance)
        GameMachine.logger.info "Player gateway sent poison pill to #{player_id}"
      end

      def create_client_connection(client_id)
        MessageLib::ClientConnection.new.set_id(client_id).
          set_gateway(self.class.name).set_server(Application.config.name)
      end

      def create_client_message(data,client_connection)
        MessageLib::ClientMessage.parse_from(data).set_client_connection(
          client_connection
        )
      end

    end
  end
end
