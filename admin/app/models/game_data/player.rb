module GameData
  class Player < ActiveRecord::Base
    has_many :entity_players
    has_many :entities, :through => :entity_players
    rails_admin do
      configure :entity_players do
        visible(false)
      end
      configure :entities do
        visible(false)
      end
      navigation_label 'Components'
      parent Entity
    end

  end
end
