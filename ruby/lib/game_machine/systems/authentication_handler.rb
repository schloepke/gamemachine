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

      def on_receive(entity_list)
        handler = EntityDispatcher.find
        player = entity_list.player
        if authenticated?(player)
          player.authenticated = true
          handler.send_message(entity_list)
        elsif authenticate(player)
          @authenticated_players[player.id] = true
          player.authenticated = true
          handler.send_message(entity_list)
        end
      end

    end
  end
end
