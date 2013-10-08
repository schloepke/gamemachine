RailsAdmin.config do |config|

  config.model 'Server' do

    navigation_label 'Configuration'
    weight 5

    edit do
      field :enabled
      field :environment
      field :name
      field :http_enabled
      field :http_host
      field :http_port
      field :tcp_enabled
      field :tcp_host
      field :tcp_port
      field :udp_enabled
      field :udp_host
      field :udp_port
      field :udt_enabled
      field :udt_host
      field :udt_port
      field :akka_host
      field :akka_port
    end

    list do
      field :environment
      field :name
      field :enabled
      field :status do
        partial true
      end
    end
  end
end
