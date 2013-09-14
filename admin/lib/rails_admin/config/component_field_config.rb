RailsAdmin.config do |config|
  config.model 'ComponentField' do
    visible(false)
    configure :component do
      visible(false)
    end
  end
end
