require_relative 'generator_base'
require 'fileutils'
class MigrationGenerator < GeneratorBase

  attr_reader :name, :fields, :class_name

  def self.create_all
    Component.all.each do |component|
      MigrationGenerator.new(component).create
    end
  end

  def self.destroy_all
    Component.all.each do |component|
      MigrationGenerator.new(component).destroy
    end
  end

  def destroy
    if has_create_migration?
      action = 'destroy'
      @migration_name = "#{action}_#{@name}"
      @migration_class_name = "#{@name.camelize}"
      template = "migration_#{action}".to_sym
      write_migration(eval_template(template))
    end
  end

  def change(action,field)
    if has_create_migration?
      @field = field
      action = action.to_s
      @migration_name = "#{action}_#{@name}_#{@field.name}"
      @migration_class_name = "#{@name.camelize}#{@field.name.camelize}"
      template = "migration_#{action}_column".to_sym
      write_migration(eval_template(template))
    end
  end

  def create
    return false if has_create_migration?
    @action = 'create'
    @migration_name = "#{@action}_#{@name}"
    write_migration(eval_template(:migration))
  end

  def move_pending_to_migrate
    Dir[File.join(pending_migrations_dir,'*.rb')].each do |pending_migration|
      FileUtils.mv(pending_migration,migration_dir)
    end
  end

  def migration_dir
    File.join( Rails.root, 'db', 'migrate')
  end

  def pending_migration_dir
    dir = File.join( Rails.root, 'db', 'pending_migrations')
    FileUtils.mkdir_p(dir)
    dir
  end

  def migrations
    Dir[File.join(migration_dir,"*.rb")]
  end

  def has_create_migration?
    migrations.each do |file|
      IO.readlines(file).each do |line|
        if line.match(/create_table :#{@name_plural}/)
          return true
        end
      end
    end
    false
  end

  private

  def write_migration(content)
    File.open(migration_file,'w') {|f| f.write(content)}
  end

  def migration_ts
    Time.now.to_s.split(" ")[0..1].join(" ").gsub!(/\D/, "")
  end

  def migration_file
    File.join(pending_migration_dir,"#{migration_ts}_#{@migration_name}.rb")
  end
end
