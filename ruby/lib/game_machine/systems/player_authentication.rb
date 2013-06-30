module GameMachine
  module Systems
    class PlayerAuthentication < Actor

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
        player = entity_list.player
        if authenticated?(player)
          player.authenticated = true
          sender.tell(entity_list,get_self)
        elsif authenticate(player)
          @authenticated_players[player.id] = true
          player.authenticated = true
          sender.tell(entity_list,get_self)
        end
      end

    end
  end
end
