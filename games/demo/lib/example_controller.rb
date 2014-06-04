module Demo
  class ExampleController < GameMachine::Actor::Base
    include GameMachine
    include GameMachine::Commands
    include GameMachine::Actor::Development

    def post_init(*args)
      GameMachine.logger.info "#{self.class.name} post_init called v7"
    end

    def on_receive(message)
      message.any
      message = Helpers::JsonObject.from_entity(message)
      GameMachine.logger.info("#{self.class.name} got #{message}")
      commands.player.send_message(message.to_entity,message.player.id)
      GameMachine.logger.info("message sent to #{message.player.id}")
    end
  end
end
