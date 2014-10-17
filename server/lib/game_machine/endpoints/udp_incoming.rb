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
        @game_handler = GameMachine::Handlers::Request.find
        @server = JavaLib::UdpServer.getUdpServer
        @server_handler = @server.getHandler
        @auth_handler = Handlers::Authentication.new
      end

      def on_receive(message)
        if message.is_a?(JavaLib::NetMessage)
          handle_incoming(message)
        end
      end

      def client_id_from_message(message)
        "#{message.host}:#{message.port}"
      end

      def handle_incoming(message)
        client_id = client_id_from_message(message)
        if message.protocol == 0
          client_message = MessageLib::ClientMessage.parse_from(message.bytes)
        elsif message.protocol == 2
          client_message = message.clientMessage
        else
          raise "Unknown protocol"
        end

        client_connection = create_client_connection(
          client_id,client_message.connection_type
        )
        client_message.set_client_connection(client_connection)

        # Login via player connect.  We send a response on success only
        if client_message.has_player_connect
          if client_message.get_player_connect.has_password
            client = create_client(message,client_connection)
            connection = create_connection(message.protocol,client_connection,client,@server,client_message.player.id)
            if outgoing_client_message = login(client_message)
              connection.send_to_client(outgoing_client_message)
            end
            return
          end
        end

        # Ensure we kill the player gateway actor on logout or on new connection
        if client_message.has_player_logout || client_message.has_player_connect
          # Ensure valid authtoken before doing anything
          unless @auth_handler.valid_authtoken?(client_message.player)
            if client_message.has_player_logout
              self.class.logger.info "Unauthenticated client #{client_message.player.id} attempting to logout"
            elsif client_message.has_player_connect
              self.class.logger.info "Unauthenticated client #{client_message.player.id} attempting to login"
            end
            return
          end

          destroy_child(client_message.player.id)
          self.class.clients.delete(client_message.player.id)
          
          if client_message.has_player_connect
            unless self.class.clients.has_key?(client_message.player.id)
              client = create_client(message,client_connection)
              self.class.clients[client_message.player.id] = client
              connection = create_connection(message.protocol,client_connection,client,@server,client_message.player.id)
              create_child(connection)
            end
          end
        end

        game_handler.send_message(
          client_message, :sender => get_self
        )
      rescue Exception => e
        self.class.logger.error "#{self.class.name} #{e.to_s}"
      end

      def create_client(message,client_connection)
        {
          :host => message.host,
          :port => message.port,
          :address => message.address,
          :ctx => message.ctx,
          :client_connection => client_connection
        }
      end

      def create_connection(protocol,client_connection,client,server,player_id)
        Connection.new(client_connection,client,server,player_id,protocol)
      end

      def login(client_message)
        client_message = client_message.clone
        player_connect = client_message.get_player_connect
        if authtoken = Handlers::PlayerAuthentication.instance.authorize(player_connect.get_player_id,player_connect.get_password)
          player_authenticated = MessageLib::PlayerAuthenticated.new.set_player_id(player_connect.get_player_id).set_authtoken(authtoken)
          client_message.set_player_connect(null)
          client_message.set_player_authenticated(player_authenticated)
          return client_message
        end
        nil
      end

      def create_child(connection)
        builder = Actor::Builder.new(Endpoints::UdpOutgoing,connection)
        builder.with_name(connection.player_id).start
        self.class.logger.debug "Starting UdpOutgoing actor #{connection.player_id}"
      end

      def destroy_child(player_id)
        Actor::Base.find(player_id).tell(JavaLib::PoisonPill.get_instance)
        self.class.logger.debug "Player gateway sent poison pill to #{player_id}"
      end

      # region and cluster connections are for when you have
      # dedicated region servers.  If you do not the connection type can be
      # left out by the client and it will default to combined.
      def client_connection_type(connection_type)
        if connection_type.nil?
          'combined'
        else
          if connection_type == 1
            'region'
          elsif connection_type == 2
            'cluster'
          else
            'combined'
          end
        end
      end
      
      def create_client_connection(client_id,connection_type)
        MessageLib::ClientConnection.new.set_id(client_id).
          set_gateway(self.class.name).
          set_server(Application.config.name).
          set_type(client_connection_type(connection_type))
      end

    end
  end
end
