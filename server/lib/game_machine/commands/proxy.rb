module GameMachine
  module Commands

    class Proxy# < Actor::Base

      def self.call_java
        JavaLib::CommandProxy.test
      end

      def self.call_ruby
      end

      def post_init(*args)

      end

      def on_receive(message)

      end

    end
  end
end
