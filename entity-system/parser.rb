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
  
  proto.getMessages.each do |message|
    klass = message.getName
    puts "Message: #{message.getName}"
    out = ERB.new(File.read(template_file),nil,'-').result(binding)
    src_file = File.join(@user_dir,'src','main','java','com','game_machine','entity_system','generated',"#{message.getName}.java")
    File.open(src_file,'w') {|f| f.write out}
    puts message.nestedEnumGroups
    message.getFields.each do |field|
      puts field.getJavaType
      puts field.toString
    end
  end
end

def write_entity(proto)
  messages = proto.getMessages.reject {|message| message.getName == 'Components'}
  template_file = File.join(@user_dir,'src','main','resources','entity.erb')
  out = ERB.new(File.read(template_file),nil,'-').result(binding)
  src_file = File.join(@user_dir,'src','main','java','com','game_machine','entity_system','generated',"Entity.java")
  File.open(src_file,'w') {|f| f.write out}
end



protofile = "#{@user_dir}/src/main/resources/messages.proto"
loader = CachingProtoLoader.new
file = java.io.File.new(protofile)
proto = ProtoUtil.parseProto(file)

write_entity(proto)
write_components(proto)

