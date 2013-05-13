require 'erb'
require 'java'
require 'pathname'

dir = File.join(Dir.getwd,'lib').gsub(File::SEPARATOR,File::ALT_SEPARATOR || File::SEPARATOR)
Dir.entries(dir).each { |jar| puts jar; require File.join(dir,jar) unless File.directory?(jar)}
  
java_import java.lang.System
java_import com.game_machine.entity_system.generated.Components

class Template
  def create_components(names)
    user_dir =  System.getProperties["user.dir"]
    template_file = File.join(user_dir,'src','main','resources','entities.erb')
    output_file = File.join(user_dir,'src','main','java','com','game_machine','entity_system','generated','EntitiesHelper.java')
    components_file = File.join(user_dir,'src','main','java','com','game_machine','entity_system','generated','Components.java')
    out = ERB.new(File.read(template_file)).result(binding)
    components_src = File.read(components_file)
    components_src.sub!('//__REPLACE_1__',out)
    File.open(components_file,'w') {|f| f.write components_src}
    true
  end
end

Template.new.create_components(Components.getFields)