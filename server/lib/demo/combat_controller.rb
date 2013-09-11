module Demo
  class CombatController < GameMachine::Actor::Base
    include GameMachine::ObjectDbProc
    include GameMachine

    aspect %w(Attack)

    dbproc(:update_health) do |entity|
      if player = entity.player
        if player.has_health
          player.health.health -= 10
        end

        send_to_player(player)
      end
      entity
    end

    def self.send_to_player(player)
        update_entity = Entity.new
        update_entity.set_id(player.id)
        update_entity.set_player(player)
        update_entity.set_send_to_player(true)
        PlayerGateway.find.tell(update_entity)
    end

    def post_init(*args)

    end

    def on_receive(message)

      if message.has_attack
        attacker = message.attack.attacker
        target = message.attack.target
        attacker = GameMachine::ObjectDb.get(attacker)
        GameMachine::ObjectDb.call_dbproc(:update_health,target,false)
      end
    end
  end
end
