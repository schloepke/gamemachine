RailsAdmin.config do |config|
  config.model 'Component' do
    navigation_label 'Configuration'
    weight 5

    label 'Component Definition'
    label_plural 'Component Definitions'

    configure :component_fields do
    end
  end
end
