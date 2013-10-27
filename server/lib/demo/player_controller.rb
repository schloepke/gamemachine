module Demo
  class MessageLib::PlayerController < GameMachine::Actor::Base

    set_player_controller

    def post_init(*args)
      entity = args.first
      @player = entity.player
      @base_health = 1000
      init_player
    end

    def init_player
    end

    def set_player_health(player)
      #player.set_health(
      #  Health.new.set_health(@base_health)
      #)
    end

    def on_receive(message)
      if message.has_player_authenticated
        if entity = GameMachine::ObjectDb.get(message.player.id)
          set_player_health(entity.player)
        else
          entity = MessageLib::Entity.new.set_id(message.player.id).
            set_player(message.player.clone)
          set_player_health(entity.player)
        end
        GameMachine::ObjectDb.put(entity)
      end
    end
  end
end
