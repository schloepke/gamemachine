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
        true
      end

      def authtoken_for(user)
        authtoken(user,'test')
      end

      private

      def authtoken(user,pass)
        Digest::MD5.hexdigest("#{user}#{pass}#{rand(10000)}")
      end
    end
  end
end

