module GameMachine
  module Systems
    class AuthenticationHandler < Actor

      def post_init
        @authenticated_players = {}
      end

      def authenticated?(player)
        (player && @authenticated_players.fetch(player.id,nil)) ? true : false
      end

      def authenticate(player)
        player.authtoken == Settings.authtoken
      end

      def register_player(client_message)
        PlayerRegistry.register_player(
          client_message.player.id,
          client_message.client_connection.client_id
        )
      end

      def handler
        EntityDispatcher.find
      end

      def on_receive(client_message)
        player = client_message.player
        player.authenticated = false
        if authenticated?(player)
          player.authenticated = true
          handler.tell(client_message)
        elsif authenticate(player)
          player.authenticated = true
          @authenticated_players[player.id] = true
          handler.tell(client_message)
          register_player(client_message)
        else
          ClientMessage.new.send_to_client
        end
      end

    end
  end
end
