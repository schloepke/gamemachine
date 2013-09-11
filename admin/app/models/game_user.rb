class GameUser < ActiveRecord::Base
  validates_presence_of :username
  validates_presence_of :password

  rails_admin do
    navigation_label 'Configuration'
    weight 5
  end
end
