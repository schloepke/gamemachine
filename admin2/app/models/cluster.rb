class Cluster < ActiveRecord::Base

  has_many :app_servers
  belongs_to :user
end
