class SystemStatus < ActiveRecord::Base

  STATUS_MESSAGE = {
    0 => 'Ok',
    1 => 'Admin restart required',
    2 => 'Server restart required',
    3 => 'Publish required',
    4 => 'Server rebuild required'
  }

  def self.status
    if status = first
      return status
    else
      create(:status => 0, :resolve_with => nil, :reason => nil)
      return first
    end
  end

  def status_text
    STATUS_MESSAGE[self.status]
  end

  def publish!
    update_attribute(:status,3)
  end

  def ok!
    update_attribute(:status,0)
  end

  def admin_restart!
    update_attribute(:status,1)
  end

  def server_restart!
    update_attribute(:status,2)
  end

  def server_rebuild!
    update_attribute(:status,4)
  end

  def ok?
    status == 0
  end

  def action_required?
    status != 0
  end

  def admin_restart?
    status == 1
  end

  def server_restart?
    status == 2
  end

  def publish?
    status == 3
  end

  def server_rebuild?
    status == 4
  end

end
