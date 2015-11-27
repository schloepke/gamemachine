require 'java'
require 'pathname'
require 'erb'
require 'fileutils'
require 'active_support/inflector'




module GameMachine
  module Protobuf
    class Generate

      attr_reader :app_root
      def initialize(app_root)
        @app_root = app_root

        jars = Dir[File.join(ENV['JAVA_ROOT'], 'local_lib', '*.jar')]
        jars.each do |jar|
          require jar
        end
        java_import java.lang.System
        java_import 'com.dyuproject.protostuff.compiler.CachingProtoLoader'
        java_import java.io.FileNotFoundException
        java_import 'com.dyuproject.protostuff.parser.ProtoUtil'
      end

      def java_root
        ENV['JAVA_ROOT']
      end
      
      def self.compile(path)
        loader = CachingProtoLoader.new
        file = java.io.File.new(path)
        ProtoUtil.parseProto(file)
      end

      def erb_template
        File.join(java_root,'component.erb')
      end

      def client_template
        File.join(java_root,'client.erb')
      end

      def model_template
        File.join(java_root,'model.erb')
      end

      def sql_template(name)
        File.join(java_root,"#{name}.rb")
      end
      
      def sql_out(name)
        File.join(config_path,'db',"#{name}_generated.sql")
      end
      
      def model_src
        File.join(java_root,'src','main','java','io', 'gamemachine','orm','models')
      end

      def server_src
        File.join(java_root,'src','main','java','io', 'gamemachine','messages')
      end

      def shared_src
        File.join(app_root,'java','shared','src','main','java','io', 'gamemachine','client','messages')
      end

      def config_path
        File.join(app_root,'config')
      end

      def write_components(proto,persistent_messages)
        messages = proto.getMessages.reject {|message| message.getName == 'Components'}
        FileUtils.mkdir_p(server_src)
        FileUtils.mkdir_p(model_src)
        FileUtils.rm_rf(shared_src)
        FileUtils.mkdir_p(shared_src)
        message_names = proto.getMessages.map {|m| m.get_name}
        messages_index = proto.getMessages.each_with_object({}) {|v,res| res[v.getName] = v}

        sqlcontent = {}
        
        proto.getMessages.each do |message|
          message_fields = []
          message_field_names = []
          message.get_fields.each do |field|
            if message_names.include?(field.getJavaType.to_s) && !field.is_repeated
              message_fields << field.get_java_type.to_s
              message_field_names << field.name
            end
          end
          unless message_fields.empty?
            #puts "#{message.getName} #{message_fields.inspect}"
          end

          klass = message.getName
          persistent = persistent_messages.include?(klass)
          #puts "Message: #{message.getName}"
          out = ERB.new(File.read(erb_template),0,'>').result(binding)
          out = out.gsub(/^(\s*\r\n){2,}/,"\r\n")
          src_file = File.join(server_src,"#{message.getName}.java")
          File.open(src_file,'w') {|f| f.write out}

          #out = ERB.new(File.read(client_template),0,'>').result(binding)
          #out = out.gsub(/^(\s*\r\n){2,}/,"\r\n")
          #src_file = File.join(shared_src,"#{message.getName}.java")
          #File.open(src_file,'w') {|f| f.write out}

          if persistent
            out = ERB.new(File.read(model_template),0,'<>').result(binding)
            out = out.gsub(/^(\s*\r\n){2,}/,"\r\n")
            out = out.gsub(/^(\s*\n){2,}/,"\n")
            src_file = File.join(model_src,"#{message.getName}.java")
            File.open(src_file,'w') {|f| f.write out}
              
            ['sqlite','postgres','mysql'].each do |dbtype|
              sqlcontent[dbtype] ||= ''
              
              sql_out = ERB.new(File.read(sql_template(dbtype)),0,'<>').result(binding)
              sql_out = sql_out.gsub(/^(\s*\r\n){2,}/,"\r\n")
              sql_out = sql_out.gsub(/^(\s*\n){2,}/,"\n")
              sqlcontent[dbtype] << sql_out
            end
          end
          message.getFields.each do |field|
            if field.isEnumField
             
            end
            #puts field.default_value_set
            #puts field.getJavaType
            
          end
        end
        
        ['sqlite','postgres','mysql'].each do |dbtype|
          File.open(sql_out(dbtype),'w') {|f| f.write sqlcontent[dbtype]}
        end
      end

      def sql_column_name(klass,field)
        "#{klass.underscore}_#{field.name.underscore}"
      end

      def sql_field(klass,field,dbtype,force_null=false)
        if dbtype == 'mysql'
          if field.isEnumField
            return "`#{sql_column_name(klass,field)}` int(11),"
          end
          txt = case field.getJavaType.to_s
          when 'boolean'
            "`#{sql_column_name(klass,field)}` tinyint(4)"
          when 'double'
            "`#{sql_column_name(klass,field)}` double"
          when 'float'
            "`#{sql_column_name(klass,field)}` float"
          when 'long'
            "`#{sql_column_name(klass,field)}` bigint"
          when 'int'
            "`#{sql_column_name(klass,field)}` int(11)"
          when 'String'
            "`#{sql_column_name(klass,field)}` varchar(128)"
          when 'ByteString'
           "`#{sql_column_name(klass,field)}` varbinary(2048)"
          end
        elsif dbtype == 'postgres'
          if field.isEnumField
            return  "#{sql_column_name(klass,field)} integer,"
          end
          txt = case field.getJavaType.to_s
          when 'boolean'
            "#{sql_column_name(klass,field)} boolean"
          when 'double'
            "#{sql_column_name(klass,field)} double precision"
          when 'float'
            "#{sql_column_name(klass,field)} double precision"
          when 'long'
            "#{sql_column_name(klass,field)} bigint"
          when 'int'
            "#{sql_column_name(klass,field)} integer"
          when 'String'
            "#{sql_column_name(klass,field)} character varying(128)"
          when 'ByteString'
            "#{sql_column_name(klass,field)} bytea"
          end
        elsif dbtype == 'sqlite'
          if field.isEnumField
            return "#{sql_column_name(klass,field)} integer,"
          end
          txt = case field.getJavaType.to_s
          when 'boolean'
            "#{sql_column_name(klass,field)} boolean"
          when 'double'
            "#{sql_column_name(klass,field)} double precision"
          when 'float'
            "#{sql_column_name(klass,field)} double precision"
          when 'long'
            "#{sql_column_name(klass,field)} integer"
          when 'int'
            "#{sql_column_name(klass,field)} integer"
          when 'String'
            "#{sql_column_name(klass,field)} varchar(128)"
          when 'ByteString'
            "#{sql_column_name(klass,field)} blob"
          end
        end

        return nil if txt.nil?

        if force_null
          return "#{txt} DEFAULT NULL,"
        end

        if field.is_required
          txt = "#{txt} NOT NULL,"
        else
          txt = "#{txt} DEFAULT NULL,"
        end
      end
      
      def simple_value?(field)
        if ['boolean','double','float','long','int','String','ByteString'].include?(field.getJavaType.to_s)
          true
        else
          false
        end
      end

      def default_value(field)
        if field.getJavaType.to_s == 'int'
          return 0
        elsif field.getJavaType.to_s == 'boolean'
            return 'false'
        elsif field.getJavaType.to_s == 'double'
          return '0D'
        elsif field.getJavaType.to_s == 'long'
          return '0L'
        elsif field.getJavaType.to_s == 'float'
          return '0F'
        elsif field.getJavaType.to_s == 'String'
          return 'null'
        else
          return nil
        end
      end
           
      def nullable(field)
        if field.getJavaType.to_s == 'int'
          return 'Integer'
        elsif ['boolean','double','long','float'].include?(field.getJavaType.to_s)
          return field.getJavaType.to_s.clone.capitalize
        else
          return field.getJavaType
        end
      end
      
      def setter(field)
        if field.getJavaType.to_s == 'int'
          return 'Integer'
        elsif ['boolean','double','long','float'].include?(field.getJavaType.to_s)
          return field.getJavaType.to_s.clone.capitalize
        else
          return field.getJavaType
        end
      end
      
      def get_type(field)
        if field.getJavaType.to_s == 'int'
          return 'int'
        elsif field.getJavaType.to_s == 'float'
          return 'float'
        elsif ['boolean','double','long'].include?(field.getJavaType.to_s)
          return field.getJavaType.to_s.clone
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
        FileUtils.rm Dir.glob(File.join(server_src,'*.java'))
        game_protofile = File.join(config_path,'game_messages.proto')
        protofile = File.join(config_path,'messages.proto')

        if File.exists?(game_protofile)
          game_messages = File.read(game_protofile)
          game_messages = game_messages.gsub('local_persistent_message','message')
          game_messages = game_messages.gsub('local_message','message')
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


