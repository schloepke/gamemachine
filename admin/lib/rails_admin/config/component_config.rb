RailsAdmin.config do |config|
  config.model 'Component' do
    navigation_label 'Game Data'
    weight 8

    label 'Component Definition'
    label_plural 'Component Definitions'

    configure :component_fields do
    end
  end
end
