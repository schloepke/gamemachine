require_relative 'generator_base'
class ComponentGenerator < GeneratorBase

  def self.create_all
    Component.all.each do |component|
      ComponentGenerator.new(component).create
    end
  end

  def self.destroy_all
    Component.all.each do |component|
      ComponentGenerator.new(component).destroy
    end
  end

  def destroy
    FileUtils.rm_f(component_file)
    FileUtils.rm_f(component_config_file)
    FileUtils.rm_f(join_model_file)
    FileUtils.rm_f(join_model_config_file)
    destroy_entity_config
    destroy_entity_associations
  end

  def update

  end

  def create
    if File.exists?(component_file)
      puts "#{@name} exists"
      return
    end
    @entity_associations_out = eval_template(:entity_associations)
    @entity_admin_block_out = eval_template(:entity_admin_block)
    @join_model_out = eval_template(:component_join_model)
    @join_model_config_out = eval_template(:component_join_model_config)
    @component_out = eval_template(:component)
    @component_config_out = eval_template(:component_config)
    create_entity_associations
    create_entity_config
    write_join_model
    write_join_model_config
    write_component
    write_component_config
  end

  private

  def component_config_file
    File.join(rails_admin_config_dir,@component_model_config_filename)
  end

  def join_model_config_file
    File.join(rails_admin_config_dir,@join_model_config_filename)
  end

  def component_file
    File.join(model_dir,@component_model_filename)
  end

  def join_model_file
    File.join(model_dir,@join_model_filename)
  end

  def write_component
    File.open(component_file,'w') {|f| f.write(@component_out)}
  end

  def write_join_model
    File.open(join_model_file,'w') {|f| f.write(@join_model_out)}
  end

  def write_join_model_config
    File.open(join_model_config_file,'w') {|f| f.write(@join_model_config_out)}
  end

  def write_component_config
    File.open(component_config_file,'w') {|f| f.write(@component_config_out)}
  end

  def entity_config_content
    file = File.join(rails_admin_config_dir,'entity_config.rb')
    File.read(file)
  end

  def destroy_entity_config
    file = File.join(rails_admin_config_dir,'entity_config.rb')
    content = File.read(file)
    content.sub!(/##{@name}_start(.*)##{@name}_end/m,'')
    File.open(file,'w') {|f| f.write(content)}
  end

  def create_entity_config
    file = File.join(rails_admin_config_dir,'entity_config.rb')
    content = File.read(file)
    content.sub!('#rails_admin_end',"#{@entity_admin_block_out}#rails_admin_end")
    File.open(file,'w') {|f| f.write(content)}
  end

  def destroy_entity_associations
    file = File.join(model_dir,'entity.rb')
    content = File.read(file)
    content.sub!(/##{@name}_start(.*)##{@name}_end/m,'')
    File.open(file,'w') {|f| f.write(content)}
  end

  def create_entity_associations
    file = File.join(model_dir,'entity.rb')
    content = File.read(file)
    content.sub!('#associations_end',"#{@entity_associations_out}#associations_end")
    File.open(file,'w') {|f| f.write(content)}
  end
end
