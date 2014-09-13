require 'forwardable'
require_relative 'public'

module GameMachine
  module AuthHandlers
    class Base
      include Singleton
      extend Forwardable

      def_delegators :@handler, :authorize, :authtoken_for

      def initialize
        @handler = Application.config.handlers.auth.constantize.new
      end

      def set_handler(handler_name)
        @handler = handler_name.constantize.new
      end
    end
  end
end

