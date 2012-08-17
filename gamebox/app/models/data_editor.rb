class DataEditor < ActiveRecord::Base
  attr_accessible :data
  
  def self.current_name
    'test16'
  end
  
  def serialize_data(data)
    self.data = Base64.encode64(Marshal.dump(data))
  end
  
  def deserialize_data
    Marshal.load(Base64.decode64(self.data))
  end
  
  def self.testdata
    rows = []
    1000.times do |x|
      rows << {:id => x}
      i = "#{x}_testing_#{x}"
      rows[x][:cell] = [x,i,i,i,i,i,i]
    end
    rows
  end
  
  def self.user_updates
    @@user_updates ||= {}
    @@user_updates
  end
  
  def self.add_update(user_id,data)
    user_updates.keys.each do |key|
      next if key == user_id
      user_updates[key] << data
    end
  end
  
end
