  class ProtobufMessage < ActiveRecord::Base
    has_many :protobuf_fields, :dependent => :destroy
    rails_admin do
      configure :protobuf_fields do
      end
    end
    def model_name_enum
      Util.component_names_for_view
    end
    #accepts_nested_attributes_for :protobuf_fields
  end

