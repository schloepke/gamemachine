
module GameMachine
  module Handlers
    class Authentication
      AUTHTOKEN = 'authorized'
      AUTHENTICATED_USERS = java.util.concurrent.ConcurrentHashMap.new

      def self.authenticated?(player)
        if authtoken = AUTHENTICATED_USERS.fetch(player.id,nil)
          return player.authtoken == authtoken
        end
        false
      end

      def self.unregister_player(player_id)
        AUTHENTICATED_USERS.delete(player_id)
      end

      def register_player(player_id)
        AUTHENTICATED_USERS[player_id] =
          authtoken_for_player(player_id)

      end

      def authtoken_for_player(player_id)
        Handlers::PlayerAuthentication.instance.authtoken_for(player_id)
      end

      def valid_authtoken?(player)
        player.authtoken == authtoken_for_player(player.id)
      end


      def authenticate!(player)
        @player = player
        if valid_authtoken?(player)
          register_player(player.id)
          player.set_authenticated(true)
        else
          false
        end
      end

    end

  end
end
