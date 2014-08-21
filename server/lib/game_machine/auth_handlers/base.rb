require 'forwardable'
require_relative 'object_store'
require_relative 'public'
require_relative 'player_register'

module GameMachine
  module AuthHandlers
    class Base
      include Singleton
      extend Forwardable

      def_delegators :@handler, :authorize, :authtoken_for

      def initialize
        @handler = Application.config.auth_handler.constantize.new
      end

      def set_handler(handler_name)
        @handler = handler_name.constantize.new
      end
    end
  end
end

