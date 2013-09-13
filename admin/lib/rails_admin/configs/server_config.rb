RailsAdmin.config do |config|

  config.model 'Server' do

    navigation_label 'Configuration'
    weight 5

    edit do
      field :environment
      field :name
      field :http_enabled
      field :http_host
      field :http_port
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
      field :status do
        pretty_value do
          server = bindings[:object]
          server.update_status
          if server.running? or server.error?
            %{<span>#{server.status_message}</span>(<a href="/admin/server/#{server.id}/stop">Stop</a>)}.html_safe
          elsif server.stopped?
            %{<span>#{server.status_message}</span>(<a href="/admin/server/#{server.id}/start">Start</a>)}.html_safe
          end
        end
      end
    end
  end
end
