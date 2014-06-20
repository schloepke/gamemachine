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
      #GameMachine.logger.info "Combatcontroller #{message}"
      if message.is_a?(Attack)
        #GameMachine.logger.info "looking up stats for #{message.target}"
        if user_stats = UserStats.find(message.target)
          GameMachine.logger.info "health #{user_stats.health}"
          dmg = damage
          if user_stats.health - dmg <= 0
            user_stats.health = 0
          else
            user_stats.health -= dmg
          end
          user_stats.save
        end
      end
    end

  end
end
