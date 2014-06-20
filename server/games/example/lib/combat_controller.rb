module Example
  class CombatController < GameMachine::Actor::Base
    include GameMachine::Commands
    include Models

    def post_init(*args)

    end

    def on_receive(message)
      #GameMachine.logger.info "Combatcontroller #{message}"
      if message.is_a?(Attack)
        #GameMachine.logger.info "looking up stats for #{message.target}"
        if user_stats = UserStats.find(message.target)
          GameMachine.logger.info "health #{user_stats.health}"
          damage = 4
          if user_stats.health - damage <= 0
            user_stats.health = 0
          else
            user_stats.health -= damage
          end
          user_stats.save
        end
      end
    end

  end
end
