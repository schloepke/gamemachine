require 'java'
require 'pathname'
require 'erb'

dir = File.join(Dir.getwd,'lib').gsub(File::SEPARATOR,File::ALT_SEPARATOR || File::SEPARATOR)
Dir.entries(dir).each do |jar|
  file = File.join(dir,jar).gsub(File::SEPARATOR,File::ALT_SEPARATOR || File::SEPARATOR)
  unless File.directory?(file)
    puts "Loading #{file}"
    require file
  end
end
  
java_import java.lang.System
java_import com.dyuproject.protostuff.compiler.CachingProtoLoader
java_import java.io.FileNotFoundException
java_import com.dyuproject.protostuff.parser.ProtoUtil

user_dir =  System.getProperties["user.dir"]
protofile = "#{user_dir}/src/main/resources/messages.proto"
loader = CachingProtoLoader.new
file = java.io.File.new(protofile)
proto = ProtoUtil.parseProto(file)

template_file = File.join(user_dir,'src','main','resources','component.erb')

proto.getMessages.each do |message|
  klass = message.getName
  puts "Message: #{message.getName}"
  out = ERB.new(File.read(template_file),nil,'-').result(binding)
  src_file = File.join(user_dir,'src','main','resources',"#{message.getName}.java")
  File.open(src_file,'w') {|f| f.write out}
  message.getFields.each do |field|
    puts field.getJavaType
    puts field.toString
  end
end