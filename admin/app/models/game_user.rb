class GameUser < ActiveRecord::Base
  validates_presence_of :user
  validates_presence_of :pass
end
