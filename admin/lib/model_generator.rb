module EntitySystem
  class ModelGenerator

    def initialize(message_file)
      @message_file = message_file
    end

    def generate
      clean
      IO.readlines(@message_file).each do |line|
        if md = line.match(/class (.*)< ::ProtocolBuffers::Message$/)
          klass_name = md[1].strip
          name = "GameMachine::Messages::#{md[1]}".strip
          klass = name.constantize
          instance = klass.new

          names = klass.fields.map do |field|
            puts field.last.type
            field.last.name.to_s
          end
          next if names.empty?

          attribute_names = names.map do |name|
            ":#{name.underscore}"
          end

          argument_names = names.map do |name|
            ":#{name} => #{name.underscore}"
          end

          argument_names = argument_names.join(', ')
          attribute_names = attribute_names.join(', ')

          template = File.read(template_file)
          template.sub!('KLASS',klass_name)
          template.gsub!('ATTRIB_METHODS',attribute_names)
          template.sub!('MESSAGE_KLASS',name)
          template.sub!('MESSAGE_ARGS',argument_names)
          #write_model_file(klass_name,template)
        end
      end
    end

    def write_model_file(klass_name,content)
      File.open(model_file(klass_name),'w') {|f| f.write(content)}
    end

    def template_file
      File.join(File.dirname(__FILE__), 'model_template.txt')
    end

    def clean
      FileList[File.join(model_dir,'*.rb')].each {|f| FileUtils.rm f}
    end

    def model_dir
      File.join(Rails.root,'app','models','entity_system')
    end

    def model_file(name)
      File.join(model_dir,"#{name.underscore}.rb")
    end
  end
end
