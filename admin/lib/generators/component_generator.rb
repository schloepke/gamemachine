class ComponentGenerator
  

  def self.generate_all
    Component.all.each do |component|
      new(component).generate
    end
  end

  def initialize(component)
    @name = component.name.downcase
    @component_model_filename = "#{@name}.rb"
    @name_plural = @name.pluralize
    @class_name = @name.classify
    @join_model_name = "entity_#{@name.pluralize}"
    @join_model_filename = "entity_#{@name}.rb"
    @fields = component.component_fields
  end


  def generate
    if File.exists?(component_file)
      puts "#{@name} exists"
      return
    end
    @entity_associations_out = eval_template(:entity_associations)
    @entity_admin_block_out = eval_template(:entity_admin_block)
    @join_model_out = eval_template(:component_join_model)
    @component_out = eval_template(:component)
    @migration_out = eval_template(:migration)
    #@protobuf_out = eval_template(:protobuf_message)
    update_entity
    write_join_model
    write_component
    write_migration
    #write_proto_message
  end

  private

  def eval_template(template)
    filename = File.join(template_dir,"#{template}.erb")
    ERB.new(File.read(filename),nil,'-').result(binding)
  end

  def template_dir
    File.join(Rails.root,'lib','generators','templates')
  end

  def model_dir
    File.join(Rails.root,'app','models','game_data')
  end

  def proto_file
    File.join(Rails.root,'db','messages.proto')
  end

  def migration_file
     ts = Time.now.to_s.split(" ")[0..1].join(" ").gsub!(/\D/, "")
    File.join( Rails.root, 'db', 'migrate',"#{ts}_create_#{@name}.rb")
  end

  def component_file
    File.join(model_dir,@component_model_filename)
  end

  def join_model_file
    File.join(model_dir,@join_model_filename)
  end

  def write_proto_message
    File.open(proto_file,'w') {|f| f.write(@protobuf_out)}
  end

  def write_migration
    File.open(migration_file,'w') {|f| f.write(@migration_out)}
  end

  def write_component
    File.open(component_file,'w') {|f| f.write(@component_out)}
  end

  def write_join_model
    File.open(join_model_file,'w') {|f| f.write(@join_model_out)}
  end

  def update_entity
    file = File.join(model_dir,'entity.rb')
    content = File.read(file)
    content.sub!('#rails_admin_end',"#{@entity_admin_block_out}#rails_admin_end")
    content.sub!('#associations_end',"#{@entity_associations_out}#associations_end")
    File.open(file,'w') {|f| f.write(content)}
  end
end
