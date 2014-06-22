module Example
  module Models
    class CombatUpdate < GameMachine::Model
      attribute :target, String
      attribute :attacker, String
      attribute :combat_ability, String
      attribute :damage, Integer
      attribute :target_health, Integer
    end
  end
end
