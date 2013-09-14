class GeneratorBase

  attr_reader :name, :fields, :class_name
  def initialize(component)
    @name = component.name.underscore
    @name_plural = @name.pluralize
    @class_name = @name.classify
    @fields = component.component_fields
    @component_model_filename = "#{@name}.rb"
    @component_model_config_filename = "#{@name}_config.rb"
    @join_model_name = "entity_#{@name.pluralize}"
    @join_model_filename = "entity_#{@name}.rb"
    @join_model_config_filename = "entity_#{@name}_config.rb"
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

  def rails_admin_config_dir
    File.join(Rails.root,'lib','rails_admin','config')
  end
end
