module Demo
  class PlayerController < GameMachine::Actor::Base

    set_player_controller

    def post_init(*args)
      @base_health = 1000
    end

    def set_player_health(player)
      player.set_health(
        Health.new.set_health(@base_health)
      )
    end

    def on_receive(message)
      if message.has_player_authenticated
        if entity = GameMachine::ObjectDb.get(message.player.id)
          unless entity.player.has_health
            set_player_health(entity.player)
            GameMachine::ObjectDb.put(entity)
          end
        else
          set_player_health(message.player)
          entity = Entity.new.set_id(message.player.id).
            set_player(message.player)
          GameMachine::ObjectDb.put(entity)
        end
      end
    end
  end
end
