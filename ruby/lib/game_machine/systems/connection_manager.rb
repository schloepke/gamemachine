module GameMachine
  module Systems
    class ConnectionManager < Actor

      register_component 'PlayerConnection'

      def initialize
        @authorized_clients = {}
      end

      def on_receive(message)
        GameMachine.logger.debug("ConnectionManager got #{message}")
      end
    end
  end
end
