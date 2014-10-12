module GameMachine
  class Killswitch < Actor::Base

    def post_init(*args)
    end

    def on_receive(message)
      if message.is_a?(MessageLib::Entity) && message.has_poison_pill
        #GameMachine::Application.akka.stop
        System.exit(0)
      end
    end

  end
end
