module Web
  module Controllers
    class AuthController < BaseController

      def get
        if auth = login(params['username'],params['password'])
          auth
        else
          GameMachine.logger.info "#{self.class.name} Login Error: #{params['username']}"
          "error"
        end
      end

      def login(user,pass)
        GameMachine::Application.auth_handler.authorize(user,pass)
      end
    end
  end
end
