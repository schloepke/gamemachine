RailsAdmin.config do |config|
  config.model 'ProtobufMessage' do
    navigation_label 'Configuration'
    weight 5

    configure :protobuf_fields do
    end
  end
end
