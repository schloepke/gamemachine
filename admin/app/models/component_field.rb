class ComponentField < ActiveRecord::Base
  belongs_to :component

  validates_presence_of :name
  validates_presence_of :value_type

  def value_type_enum
    ['integer','float','string','text','boolean']
  end
end

