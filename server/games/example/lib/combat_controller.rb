module Example
  class CombatController < GameMachine::Actor::Base
    include GameMachine::Commands
    include Models

    def post_init(*args)

    end

    def hit?
      chance = rand(0..10)
      chance >= 5 ? true : false
    end

    def damage
      rand(0..4)
    end

    def on_receive(message)
      if message.is_a?(Attack)
        attack = message
        attacker_vitals = Vitals.find(attack.attacker)
        target_vitals = Vitals.find(attack.target)
        #GameMachine.logger.info "attack #{attack.attributes.inspect} vitals #{target_vitals.attributes.inspect}"

        # Already dead
        if target_vitals.health <= 0
          return
        end

        if attacker_vitals.entity_type == 'player'
          player_id = attack.attacker
        else
          player_id = attack.target
        end

        if hit?
          dmg = damage
          if target_vitals.health - dmg <= 0
            target_vitals.health = 0
          else
            target_vitals.health -= dmg
            send_combat_update(attack,target_vitals,player_id,dmg)
            GameMachine.logger.info "entity #{target_vitals.id} hit for #{dmg} damage"
          end
          target_vitals.save
        end
      end
    end

    def send_combat_update(attack,target_vitals,player_id,dmg)
      combat_update = CombatUpdate.new(
        :target => attack.target,
        :attacker => attack.attacker,
        :damage => dmg,
        :target_health => target_vitals.health
      )
      commands.player.send_message(combat_update,player_id)

      # Every combat update goes to all players nearby
      neighbors = commands.grid.get_neighbors_for(target_vitals.id)
      neighbors.each do |neighbor|
        commands.player.send_message(combat_update,neighbor.id)
      end
    end

  end
end
