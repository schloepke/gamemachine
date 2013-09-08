module GameData
  class EntityPlayer < ActiveRecord::Base

    belongs_to :player
    belongs_to :entity
    rails_admin do
      visible(false)
    end
  end
end
