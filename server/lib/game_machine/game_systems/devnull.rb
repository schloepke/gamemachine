module GameMachine
  module GameSystems
    class Devnull < Actor::Base
      

      def on_receive(message)
        GameMachine.logger.debug("Devnull got #{message}")
      end
    end
  end
end
