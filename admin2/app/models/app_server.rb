class AppServer < ActiveRecord::Base

  belongs_to :cluster
  belongs_to :user

  def environment_enum
    ['development','production','test']
  end

  def data_store_enum
    ['couchbase','mapdb','memory']
  end
end
