class ProtoGenerator
  
  class << self
    def clear_messages
      write_proto_messages('')
    end

    def publish
      write_proto_messages(generate_all)
    end

    def generate_all
      messages = ''
      ProtobufMessage.all.each do |message|
        messages << new(message).generate
      end
      messages
    end

    def proto_file
      File.join(Rails.root,'../server','config','game_messages.proto')
    end

    def write_proto_messages(messages)
      File.open(proto_file,'w') {|f| f.write(messages)}
    end
  end

  def initialize(message)
    @name = message.name.downcase
    @name_plural = @name.pluralize
    @class_name = @name.classify
    @fields = message.protobuf_fields
  end

  def generate
    eval_template(:protobuf_message)
  end

  private

  def template_dir
    File.join(Rails.root,'lib','generators','templates')
  end

  def eval_template(template)
    filename = File.join(template_dir,"#{template}.erb")
    ERB.new(File.read(filename),nil,'-').result(binding)
  end

end

