require 'fileutils'
class GeneratorBase

  class << self
    def gen_base_path
      File.join(Rails.root,'generated')
    end

    def gen_configs_path
      File.join(gen_base_path,'configs')
    end

    def gen_models_path
      File.join(gen_base_path,'models')
    end

    def gen_gamedata_models_path
      File.join(gen_models_path,'game_data')
    end

    def gen_migrations_path
      File.join(gen_base_path,'migrate')
    end

    def ensure_gen_paths
      FileUtils.mkdir_p gen_gamedata_models_path
      FileUtils.mkdir_p gen_configs_path
      FileUtils.mkdir_p gen_migrations_path
    end

    def ensure_entity_config
      entity_config = File.join(Rails.root,'lib','rails_admin','config','entity_config.rb')
      generated_entity_config = File.join(gen_configs_path,'entity_config.rb')
      unless File.exists?(generated_entity_config)
        FileUtils.cp entity_config,generated_entity_config
      end
    end
  end

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

  def generated_model_dir
    self.class.gen_gamedata_models_path
  end

  def model_dir
    File.join(Rails.root,'app','models','game_data')
  end

  def rails_admin_generated_config_dir
    self.class.gen_configs_path
  end

  def rails_admin_config_dir
    File.join(Rails.root,'lib','rails_admin','config')
  end
end
