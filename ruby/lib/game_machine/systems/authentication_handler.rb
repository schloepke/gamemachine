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
          client_message.client_connection
        )
        PlayerRegistry.register_observer(
          client_message.player.id,ActorRef.new(get_self)
        )
      end

      def handler
        EntityDispatcher.find
      end

      def on_receive(message)
        if message.is_a?(Disconnected)
          @authenticated_players[message.player_id] = false
        else
          player = message.player
          player.authenticated = false
          if authenticated?(player)
            player.authenticated = true
            handler.tell(message)
          elsif authenticate(player)
            player.authenticated = true
            @authenticated_players[player.id] = true
            register_player(message)
            handler.tell(message)
          else
            error_message = Helpers::GameMessage.new(player.id)
          end
        end
      end

    end
  end
end
