class DataEditor < ActiveRecord::Base
  attr_accessible :data
  
  def serialize_data(data)
    self.data = Base64.encode64(Marshal.dump(data))
  end
  
  def deserialize_data
    Marshal.load(Base64.decode64(self.data))
  end
  
  def self.testdata
    rows = []
    500.times do |x|
      rows << {:id => x}
      i = "#{x}_testing_#{x}"
      rows[x][:cell] = [x,i,i,i,i,i,i]
    end
    rows
  end
  
end
