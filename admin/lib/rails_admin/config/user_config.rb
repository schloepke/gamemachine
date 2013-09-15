RailsAdmin.config do |config|
  config.model 'User' do
    navigation_label 'System'
    weight 1000
    visible(false)
  end
end
