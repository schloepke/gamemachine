module GameMachine
  module GameSystems
    class RemoteEcho < Actor::Base
      include GameMachine::Commands
      aspect %w(EchoTest)

      def post_init(*args)
        chars = [*('a'..'z'),*('0'..'9')].flatten
        str = Array.new(400) {|i| chars.sample}.join
        @test_message = MessageLib::Entity.new.set_id(str)
        #schedule_message('test',10)
      end

      def on_receive(message)
        if message.is_a?(String) && message == 'test'
          return
        end
        GameMachine.logger.info "#{self.class.name} #{message}"
        commands.player.send_message(message,message.player.id)
      end
    end
  end
end

