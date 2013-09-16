require 'digest/md5'
module GameMachine
  module AuthHandlers
    class Basic

      def initialize
        load_users
      end

      def load_users
        @users = {}
        @sessions = {}
        GameData.data.fetch(:game_users,[]).each do |user|
          @users[user['user']] = user['pass']
          if user['auth_token']
            @sessions[user['user']] = user['auth_token']
          end
        end

      end

      def authorize(user,pass)
        if password = @users.fetch(user,nil)
          if pass == password
            @sessions[user] = authtoken(user,pass)
            return @sessions[user]
          end
        end
        false
      end

      def authtoken_for(user)
        @sessions.fetch(user,nil)
      end

      private

      def authtoken(user,pass)
        Digest::MD5.hexdigest("#{user}#{pass}#{rand(10000)}")
      end
    end
  end
end

