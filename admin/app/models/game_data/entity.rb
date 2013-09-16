module GameData
  class Entity < ActiveRecord::Base
    #combat_ability_start
    has_many :entity_combat_abilities
    has_many :combat_abilities, :through => :entity_combat_abilities
    #combat_ability_end
    #effect_start
    has_many :entity_effects
    has_many :effects, :through => :entity_effects
    #effect_end
    #attack_start
    has_many :entity_attacks
    has_many :attacks, :through => :entity_attacks
    #attack_end
#associations_end

    #accepts_nested_attributes_for :players, :allow_destroy => true

  end
end
