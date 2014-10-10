
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

      def register_player(player)
        AUTHENTICATED_USERS[player.id] =
          authtoken_for_player(player)

      end

      def authtoken_for_player(player)
        if public?
          player.authtoken
        else
          Handlers::PlayerAuthentication.instance.authtoken_for(player.id)
        end
      end

      def public?
        Handlers::PlayerAuthentication.instance.public?
      end

      def valid_authtoken?(player)
        public? || player.authtoken == authtoken_for_player(player)
      end


      def authenticate!(player)
        @player = player
        if valid_authtoken?(player)
          register_player(player)
          player.set_authenticated(true)
        else
          GameMachine.logger.warn("Authentication for #{player.id} failed")
          false
        end
      end

    end

  end
end
