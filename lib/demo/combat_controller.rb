module Demo
  class CombatController < GameMachine::Actor::Base

    aspect %w(Attack)

    def post_init(*args)

    end

    def on_receive(message)

      if message.has_attack
        attacker = message.attack.attacker
        target = message.attack.target
        #puts "attacker=#{attacker} target=#{target}"
        attacker = GameMachine::ObjectDb.get(attacker)
        if target_entity = GameMachine::ObjectDb.get(target)
          player = target_entity.player
          if player.has_health
            player.health.health -= 10
            GameMachine::ObjectDb.put(target_entity)
          end
        end
      end
    end
  end
end
