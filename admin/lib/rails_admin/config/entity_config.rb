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
      
      
      
      
      
      
      
      
      
#rails_admin_end
  end
end
