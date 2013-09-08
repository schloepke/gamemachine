module GameData
  class EntityCombatAbility < ActiveRecord::Base
    belongs_to :combat_ability
    belongs_to :entity
    rails_admin do
      visible(false)
    end
  end
end
