class Server < ActiveRecord::Base
  validates_presence_of :akka_host
  validates_presence_of :akka_port
  validates_presence_of :name
  validates_presence_of :environment
  validates_uniqueness_of :name, :scope => [:environment]
  
  before_save :set_status

  rails_admin do
    navigation_label 'Configuration'
    weight 5

    edit do
      field :environment
      field :name
      field :http_enabled
      field :http_host
      field :http_port
      field :udp_enabled
      field :udp_host
      field :udp_port
      field :udt_enabled
      field :udt_host
      field :udt_port
      field :akka_host
      field :akka_port
    end

    list do
      field :environment
      field :name
      field :status
    end
  end

  def set_status
    if status.nil?
      status_will_change!
      self.status = 'Not running'
    end
  end

  def environment_enum
    ['development','production','test']
  end

end
