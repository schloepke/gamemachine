class ProtobufMessage < ActiveRecord::Base
  has_many :protobuf_fields, :dependent => :destroy
  rails_admin do
    navigation_label 'Game Data'
    weight -1

    configure :protobuf_fields do
    end
  end

  accepts_nested_attributes_for :protobuf_fields, :allow_destroy => true

end
