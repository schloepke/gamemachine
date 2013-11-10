class AppServer < ActiveRecord::Base

  belongs_to :cluster
  belongs_to :user

  def self.template
    includes(:user).where(:name => 'default').
      where("users.email = ?",'template@gamemachine.io').
      references(:user).first
  end

  def environment_enum
    ['development','production','test']
  end

  def data_store_enum
    ['couchbase','mapdb','memory']
  end
end
