module GameData
  class EntityCombatAbility < ActiveRecord::Base
    belongs_to :combat_ability
    belongs_to :entity
  end
end

