module GameData
  class Entity < ActiveRecord::Base
    #combat_ability
    has_many :entity_combat_abilities
    has_many :combat_abilities, :through => :entity_combat_abilities
#associations_end

    rails_admin do
      navigation_label 'Game Data'
      weight 10
      #combat_ability
      configure :entity_combat_abilities do
        visible(false)
      end
      configure :combat_abilities do
      end
#rails_admin_end
    end

    #accepts_nested_attributes_for :players, :allow_destroy => true

  end
end
