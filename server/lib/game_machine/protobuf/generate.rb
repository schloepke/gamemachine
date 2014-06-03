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

      def erb_template
        File.join(app_root,'lib','game_machine', 'protobuf','component.erb')
      end

      def java_src
        File.join(app_root,'java','src','main','java','com',
                  'game_machine','entity_system','generated')
      end

      def config_path
        File.join(app_root,'config')
      end

      def write_components(proto)
        messages = proto.getMessages.reject {|message| message.getName == 'Components'}
        FileUtils.mkdir_p(java_src)
        proto.getMessages.each do |message|
          klass = message.getName
          #puts "Message: #{message.getName}"
          out = ERB.new(File.read(erb_template),nil,'-').result(binding)
          src_file = File.join(java_src,"#{message.getName}.java")
          File.open(src_file,'w') {|f| f.write out}
          #message.getFields.each do |field|
          #  puts field.getJavaType
          #  puts field.toString
          #end
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
          gm = Protobuf::GameMessages.new(game_protofile)
          entity_fields = gm.create_entity_fields
          game_entity_fields = entity_fields.join("\n")
        else
          game_messages = ''
          game_entity_fields = ''
        end
        messages = File.read(protofile)

        combined_messages = messages.sub('//GAME_MESSAGES',game_messages)
        combined_messages = combined_messages.sub('//GAME_ENTITY_MESSAGES',game_entity_fields)
        combined_messages_protofile = File.join(config_path,'combined_messages.proto')

        File.open(combined_messages_protofile,'w') {|f| f.write(combined_messages)}

        loader = CachingProtoLoader.new
        file = java.io.File.new(combined_messages_protofile)
        proto = ProtoUtil.parseProto(file)

        write_components(proto)
        #FileUtils.rm(combined_messages_protofile)
        combined_messages
      end
    end
  end
end


