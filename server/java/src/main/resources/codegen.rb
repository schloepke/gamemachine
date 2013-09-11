require 'java'
require 'pathname'
require 'erb'
require 'fileutils'
require 'protostuff-compiler-1.0.7-jarjar.jar'

java_import java.lang.System
java_import com.dyuproject.protostuff.compiler.CachingProtoLoader
java_import java.io.FileNotFoundException
java_import com.dyuproject.protostuff.parser.ProtoUtil

@user_dir =  System.getProperties["user.dir"]
   
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

def write_components(proto)
  template_file = File.join(@user_dir,'src','main','resources','component.erb')
  messages = proto.getMessages.reject {|message| message.getName == 'Components'}
  proto.getMessages.each do |message|
    klass = message.getName
    #puts "Message: #{message.getName}"
    out = ERB.new(File.read(template_file),nil,'-').result(binding)
    src_file = File.join(@user_dir,'src','main','java','com','game_machine','entity_system','generated',"#{message.getName}.java")
    File.open(src_file,'w') {|f| f.write out}
    #message.getFields.each do |field|
    #  puts field.getJavaType
    #  puts field.toString
    #end
  end
end

game_protofile = File.join(@user_dir,'../../admin/db/messages.proto')
protofile = File.join(@user_dir,'/src/main/resources/messages.proto')

if File.exists?(game_protofile)
  game_messages = File.read(game_protofile)
else
  game_messages = ''
end
messages = File.read(protofile)

combined_messages = messages.sub('//GAME_MESSAGES',game_messages)
combined_messages_protofile = File.join(@user_dir,'combined_messages.proto')

File.open(combined_messages_protofile,'w') {|f| f.write(combined_messages)}

loader = CachingProtoLoader.new
file = java.io.File.new(combined_messages_protofile)
proto = ProtoUtil.parseProto(file)

write_components(proto)
FileUtils.rm(combined_messages_protofile)

