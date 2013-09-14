class Component < ActiveRecord::Base
  has_many :component_fields, :dependent => :destroy

  validates_uniqueness_of :name
  accepts_nested_attributes_for :component_fields, :allow_destroy => true

  before_save :downcase_name

  before_destroy  :track_destroy
  after_create    :track_create

  def track_create
    ComponentGenerator.new(self).create
    MigrationGenerator.new(self).create
  end

  def track_destroy
    ComponentGenerator.new(self).destroy
    MigrationGenerator.new(self).destroy
  end

  def downcase_name
    self.name.downcase!
  end

  def model_name_enum
    Util.component_names_for_view
  end
end

