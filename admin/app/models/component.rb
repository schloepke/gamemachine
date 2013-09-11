  class Component < ActiveRecord::Base
    has_many :component_fields, :dependent => :destroy
    rails_admin do
      navigation_label 'Game Data'
      weight 8
      label 'Component Definition'
      label_plural 'Component Definitions'

      configure :component_fields do
      end
    end
    def model_name_enum
      Util.component_names_for_view
    end

    validates_uniqueness_of :name
    accepts_nested_attributes_for :component_fields, :allow_destroy => true

    before_save :downcase_name

    def downcase_name
      self.name.downcase!
    end
  end

