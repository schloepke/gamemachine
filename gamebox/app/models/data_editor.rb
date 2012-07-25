class DataEditor < ActiveRecord::Base
  # attr_accessible :title, :body
  serialize :data
  
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
