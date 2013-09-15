RailsAdmin.config do |config|
  config.model 'Asset' do
    navigation_label 'Game Data'
    weight 10
    field :asset, :carrierwave
    field :name
    field :version
  end
end
