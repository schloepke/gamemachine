module GameData
  class CombatAbility < ActiveRecord::Base
    has_many :entity_combat_abilities
    has_many :entities, :through => :entity_combat_abilities
    rails_admin do
      navigation_label 'Components'
      parent Entity
      configure :entity_combat_abilities do
        visible(false)
      end
      configure :entities do
        visible(false)
      end

    end
  end
end
