require 'digest/md5'
module GameMachine
  module AuthHandlers
    class Public

      def initialize
        @sessions = {}
      end

      def add_user(username,authtoken)
        @sessions[username] = authtoken
      end

      def authorize(user,pass)
        authtoken(user,'public')
      end

      def authtoken_for(user)
        if @sessions.has_key?(user)
          @sessions[user]
        else
          @sessions[user] = authtoken(user,'public')
        end
      end

      private

      def authtoken(user,pass)
        Digest::MD5.hexdigest("#{user}#{pass}#{rand(10000)}")
      end
    end
  end
end

