class ProtoGenerator
  
  class << self

    def current_version
      39
    end

    def clear_messages
      write_proto_messages('')
    end

    def publish
      write_proto_messages(generate_all)
      generate_entity_messages
    end

    def generate_entity_messages
      version = current_version
      messages = ''
      ProtobufMessage.all.each do |message|
        version += 1
        messages << new(message,version).entity_message << "\n"
      end
      write_entity_messages(messages)
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

    def entity_messages_file
      File.join(Rails.root,'../server','config','game_entity_messages.proto')
    end

    def write_entity_messages(messages)
      File.open(entity_messages_file,'w') {|f| f.write(messages)}
    end

    def write_proto_messages(messages)
      File.open(proto_file,'w') {|f| f.write(messages)}
    end
  end

  def initialize(message,current_version=self.class.current_version)
    @current_version = current_version
    @name = message.name
    @entity_message_name = message.name.camelize(:lower)
    @fields = message.protobuf_fields
  end

  def entity_message
    "optional #{@name} #{@entity_message_name} = #{@current_version};"
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

