class ComponentField < ActiveRecord::Base
  belongs_to :component

  validates_presence_of :name
  validates_presence_of :value_type

  before_destroy  :track_destroy
  after_create    :track_create

  def track_create
    MigrationGenerator.new(component).change(:add,self)
    SystemStatus.status.admin_restart!
  end

  def track_destroy
    MigrationGenerator.new(component).change(:remove,self)
    SystemStatus.status.admin_restart!
  end

  def value_type_enum
    ['integer','float','string','text','boolean']
  end
end

