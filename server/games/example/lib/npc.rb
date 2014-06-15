module Example
  class Npc < GameMachine::Actor::Base
    include GameMachine::Actor::Development

    def post_init(*args)

      schedule_message('update',20)
    end

    def on_receive(message)

    end
  end
end
