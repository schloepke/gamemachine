module GameData
  class Entity < ActiveRecord::Base
    
    #combat_ability_start
    has_many :entity_combat_abilities
    has_many :combat_abilities, :through => :entity_combat_abilities
    #combat_ability_end
#associations_end

    #accepts_nested_attributes_for :players, :allow_destroy => true

  end
end
