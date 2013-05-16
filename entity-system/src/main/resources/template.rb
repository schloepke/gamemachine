require 'erb'
require 'java'
require 'pathname'

dir = File.join(Dir.getwd,'lib').gsub(File::SEPARATOR,File::ALT_SEPARATOR || File::SEPARATOR)
Dir.entries(dir).each do |jar|
  file = File.join(dir,jar).gsub(File::SEPARATOR,File::ALT_SEPARATOR || File::SEPARATOR)
  unless File.directory?(file)
    puts "Loading #{file}"
    require file
  end
end
  
java_import java.lang.System
java_import com.game_machine.entity_system.generated.Components

class Template
  
  def create_components
    components_fields = Components.getFields
    src_files = components_fields.clone
    src_files << 'components'
    src_class_names = src_files.collect {|name| name.slice(0,1).capitalize + name.slice(1..-1)}
    
    user_dir =  System.getProperties["user.dir"]
    template_file = File.join(user_dir,'src','main','resources','entities.erb')
    
    src_class_names.each do |klass|
      src_file = File.join(user_dir,'src','main','java','com','game_machine','entity_system','generated',"#{klass}.java")
      if klass == 'Components'
        out = ERB.new(File.read(template_file)).result(binding)
      else
        out = ''
      end
      
      src = File.read(src_file)
      src.sub!(/\/\/__REPLACE_1_START__.*\/\/__REPLACE_1_END__/m,out)
      File.open(src_file,'w') {|f| f.write src}
    end
    
    true
  end
end

Template.new.create_components