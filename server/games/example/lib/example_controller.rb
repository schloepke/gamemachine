module Example
  class ExampleController < GameMachine::Actor::Base
    include GameMachine
    include GameMachine::Commands
    include GameMachine::Actor::Development

    # This is our initializer method. It is called once on actor start.
    #
    attr_reader :game_data
    def post_init(*args)

      # Pull the game data out of args
      @game_data = args.first

      # Just because
      GameMachine.logger.info "#{self.class.name} post_init called"

    end

    def on_receive(message)

      # Send message back to player who sent it
      commands.player.send_message(message.to_entity,message.player.id)
      GameMachine.logger.info("message sent to #{message.player.id}")
    end
  end
end
