RailsAdmin.config do |config|
  config.model 'ProtobufMessage' do
    navigation_label 'Game Data'
    weight 9

    configure :protobuf_fields do
    end
  end
end
