module GameData
  class CombatAbility < ActiveRecord::Base
    has_many :entity_combat_abilities
    has_many :entities, :through => :entity_combat_abilities
  end
end

