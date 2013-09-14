class Server < ActiveRecord::Base
  validates_presence_of :akka_host
  validates_presence_of :akka_port
  validates_presence_of :name
  validates_presence_of :environment
  validates_uniqueness_of :name, :scope => [:environment]
  validate :one_server_enabled

  STATUS_MESSAGE = {
    0 => 'Stopped',
    1 => 'Starting',
    2 => 'Running',
    3 => 'Unknown/Error'
  }

  def self.already_enabled?
    where(:enabled => true).size >= 1
  end

  def one_server_enabled
    if enabled && self.class.already_enabled?
      errors.add(:enabled, "Cannot be set on more then one server")
    end
  end

  def environment_enum
    ['development','production','test']
  end

  def status_message
    STATUS_MESSAGE[status]
  end

  def running?
    status == 2
  end

  def stopped?
    status == 0
  end

  def starting?
    status == 1
  end

  def error?
    status == 3
  end

  def update_status
    out = `cd #{::GAME_MACHINE_ROOT};sh bin/game_machine.sh status`
    if out.match(/stale pidfile/)
      new_status = 3
    elsif out.match(/is not running/)
      new_status = 0
    elsif out.match(/is running/)
      new_status = 2
    end
    update_attribute(:status,new_status)
  end

  def start
    system("cd #{::GAME_MACHINE_ROOT};sh bin/game_machine.sh start --name=#{self.name}")
    update_status
  end

  def stop
    system("cd #{::GAME_MACHINE_ROOT};sh bin/game_machine.sh stop")
    update_status
  end
end
