module Demo
  class CombatController < GameMachine::Actor::Base

    aspect %w(Attack)

    def post_init(*args)

    end

    def on_receive(message)

      if message.has_attack

        attacker = message.attack.attacker
        target = message.attack.target

        attacker = GameMachine::ObjectDb.get(attacker)
        target = GameMachine::ObjectDb.get(target)
      end
    end
  end
end
