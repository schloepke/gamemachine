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
        true
      end

      def on_receive(client_message)
        handler = EntityDispatcher.find
        player = client_message.player
        player.authenticated = false
        if authenticated?(player)
          player.authenticated = true
          handler.send_message(client_message)
        elsif authenticate(player)
          @authenticated_players[player.id] = true
          player.authenticated = true
          handler.send_message(client_message)
        end
      end

    end
  end
end
