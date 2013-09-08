module GameData
  class Entity < ActiveRecord::Base
    # Generated associations block
    has_many :entity_players
    has_many :players, :through => :entity_players
    has_many :entity_combat_abilities
    has_many :combat_abilities, :through => :entity_combat_abilities

    # Generated rails admin block
    rails_admin do
      navigation_label 'Game Data'
      weight -1
      configure :entity_players do
        visible(false)
      end
      configure :entity_combat_abilities do
        visible(false)
      end
      configure :combat_abilities do
      end
      configure :players do
      end
    end
  end
end
