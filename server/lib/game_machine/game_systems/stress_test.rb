module GameMachine
  module GameSystems
    class StressTest < Actor::Base
      include GameMachine::Commands

      attr_reader :grid
      def post_init(*args)
      end

      def on_receive(message)
        #GameMachine.logger.info "#{self.class.name} #{message}"
        commands.player.send_message(message,message.player.id)

        #grid.set(message.player.id,2.0,1.0,'all')
        #grid.neighbors(1.0,4.0,'all')
      end
    end
  end
end

