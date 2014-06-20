module Web
  module Controllers
    class PlayerRegisterController < BaseController

      def register_class
        GameMachine::Application.config.player_register_handler.constantize
      end

      def create
        register_class.new(params).register!
      end

    end
  end
end
