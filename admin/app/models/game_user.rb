class GameUser < ActiveRecord::Base
  validates_presence_of :user
  validates_presence_of :pass

  rails_admin do
    navigation_label 'Configuration'
    weight 5
  end
end
