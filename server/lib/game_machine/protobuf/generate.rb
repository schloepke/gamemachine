require 'java'
require 'pathname'
require 'erb'
require 'fileutils'

java_import java.lang.System
java_import com.dyuproject.protostuff.compiler.CachingProtoLoader
java_import java.io.FileNotFoundException
java_import com.dyuproject.protostuff.parser.ProtoUtil

module GameMachine
  module Protobuf
    class Generate

      attr_reader :app_root
      def initialize(app_root)
        @app_root = app_root
      end

      def self.compile(path)
        loader = CachingProtoLoader.new
        file = java.io.File.new(path)
        ProtoUtil.parseProto(file)
      end

      def erb_template
        File.join(app_root,'java','component.erb')
      end

      def model_template
        File.join(app_root,'java','model.erb')
      end

      def model_src
        File.join(app_root,'java','src','main','java','com', 'game_machine','orm','models')
      end

      def java_src
        File.join(app_root,'java','src','main','java','GameMachine', 'Messages')
      end

      def config_path
        File.join(app_root,'config')
      end

      def write_components(proto,persistent_messages)
        messages = proto.getMessages.reject {|message| message.getName == 'Components'}
        FileUtils.mkdir_p(java_src)
        FileUtils.mkdir_p(model_src)
        proto.getMessages.each do |message|
          klass = message.getName
          persistent = persistent_messages.include?(klass)
          #puts "Message: #{message.getName}"
          out = ERB.new(File.read(erb_template),0,'>').result(binding)
          src_file = File.join(java_src,"#{message.getName}.java")
          File.open(src_file,'w') {|f| f.write out}

          if persistent
            out = ERB.new(File.read(model_template),0,'>').result(binding)
            src_file = File.join(model_src,"#{message.getName}.java")
            File.open(src_file,'w') {|f| f.write out}
          end
          #message.getFields.each do |field|
          #  puts field.getJavaType
          #  puts field.toString
          #end
        end
      end

      def simple_value?(field)
        if ['boolean','double','float','long','int','String'].include?(field.getJavaType.to_s)
          true
        else
          false
        end
      end

      def get_type(field)
        if field.getJavaType.to_s == 'int'
          return 'Integer'
        elsif ['boolean','double','float','long'].include?(field.getJavaType.to_s)
          return field.getJavaType.to_s.clone.capitalize
        else
          return field.getJavaType
        end
      end

      def varname(name)
        name.slice(0,1).downcase + name.slice(1..-1)
      end

      def classname(name)
        name.slice(0,1).capitalize + name.slice(1..-1)
      end

      def generate
        game_protofile = File.join(config_path,'game_messages.proto')
        protofile = File.join(config_path,'messages.proto')

        if File.exists?(game_protofile)
          game_messages = File.read(game_protofile)
          game_messages = game_messages.gsub('persistent_message','message')
          gm = Protobuf::GameMessages.new(game_protofile)
          persistent_messages = gm.persistent_messages
          entity_fields = gm.create_entity_fields
          game_entity_fields = entity_fields.join("\n")
        else
          game_messages = ''
          game_entity_fields = ''
          persistent_messages = []
        end
        messages = File.read(protofile)

        combined_messages = messages.sub('//GAME_MESSAGES',game_messages)
        combined_messages = combined_messages.sub('//GAME_ENTITY_MESSAGES',game_entity_fields)
        combined_messages_protofile = File.join(config_path,'combined_messages.proto')

        File.open(combined_messages_protofile,'w') {|f| f.write(combined_messages)}

        # test message defintion validity
        #tmp_dir = File.join('/tmp/proto_test')
        #FileUtils.rm_rf(tmp_dir)
        #FileUtils.mkdir_p(tmp_dir)
        #unless system("protoc #{combined_messages_protofile} --java_out=#{tmp_dir}")
        #  return false
        #end

        # This just stops generating code when it hits an error, but does not
        # throw an exception
        proto = self.class.compile(combined_messages_protofile)

        write_components(proto,persistent_messages)
        #FileUtils.rm(combined_messages_protofile)
        combined_messages
      end
    end
  end
end


