module Example
  class ZoneManager < GameMachine::Actor::Base

    def post_init(*args)

    end

    def on_receive(message)
      if message.is_a?(GameMachine::Models::Region)
        if message.manager == self.class.name
          # This is us, it means we are responsible for the specified zone
          zone = message.name
          GameMachine.logger.info "#{self.class.name} zone #{zone} up"
        end
      end
    end
  end
end
