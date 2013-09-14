RailsAdmin.config do |config|
  config.model 'ProtobufField' do
    visible(false)
    configure :protobuf_message do
      visible(false)
    end
  end
end
