class ProtobufMessage < ActiveRecord::Base
  has_many :protobuf_fields, :dependent => :destroy
  rails_admin do
    navigation_label 'Game Data'
    weight 9

    configure :protobuf_fields do
    end
  end

  validates_uniqueness_of :name
  accepts_nested_attributes_for :protobuf_fields, :allow_destroy => true

  def self.game_messages
    @messages = []
    java_dir = File.join(GAME_MACHINE_ROOT, 'java')
    protofile = File.join(java_dir,'src','main','resources','messages.proto')

    IO.readlines(protofile).each do |line|
      if md = line.match(/message\s+([a-zA-Z0-9]{1,128})\s+{\s+$/)
        message_name = md[1].strip
        @messages << message_name
      end
    end
    @messages
  end

end
