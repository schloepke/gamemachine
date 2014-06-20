module Example
  module Models
    class Attack < GameMachine::Model
      attribute :target, String
      attribute :attacker, String
      attribute :combat_ability, String
    end
  end
end
