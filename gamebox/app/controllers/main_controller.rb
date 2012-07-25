class MainController < ApplicationController

  def update_data
    render :json => {}.to_json
  end
  
  def data
    rows = []
    2000.times do |x|
      rows << {:id => x}
      i = "#{x}_testing"
      rows[x][:cell] = [x,i,i,i,i,i,i]
    end
    data = {:page => 1, :records => rows.size, :rows => rows, :total => 1}
    render :json => data.to_json
  end
  
  def index
    
  end

end
