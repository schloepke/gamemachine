class ComponentField < ActiveRecord::Base
  belongs_to :component

  validates_presence_of :name
  validates_presence_of :value_type
  rails_admin do
    visible(false)
    configure :component do
      visible(false)
    end
  end

  def value_type_enum
    ['integer','float','string','text','boolean']
  end
end

