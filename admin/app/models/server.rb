class Server < ActiveRecord::Base
  validates_presence_of :akka_host
  validates_presence_of :akka_port
  validates_presence_of :name
  validates_uniqueness_of :name, :scope => [:environment]
  
  rails_admin do
    navigation_label 'Configuration'
    weight 5
  end

end
