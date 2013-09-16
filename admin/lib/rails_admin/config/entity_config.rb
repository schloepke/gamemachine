RailsAdmin.config do |config|
  config.model 'GameData::Entity' do
    navigation_label 'Game Data'
    weight 10
      
      
      
      
      
      #combat_ability_start
      configure :entity_combat_abilities do
        visible(false)
      end
      configure :combat_abilities do
      end
      #combat_ability_end
      #effect_start
      configure :entity_effects do
        visible(false)
      end
      configure :effects do
      end
      #effect_end
      #attack_start
      configure :entity_attacks do
        visible(false)
      end
      configure :attacks do
      end
      #attack_end
#rails_admin_end
  end
end
