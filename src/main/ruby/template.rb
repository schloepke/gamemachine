require 'erb'
require 'yaml'
require 'java'

java_import java.lang.System

class Template
  def create_components(names)
    user_dir =  System.getProperties["user.dir"]
    template_file = File.join(user_dir,'src','main','resources','entities.erb')
    output_file = File.join(user_dir,'src','main','resources','Entities.java')
    out = ERB.new(File.read(template_file)).result(binding)
    File.open(output_file,'w') {|f| f.write out}
    true
  end
end

Template.new