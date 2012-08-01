class MainController < ApplicationController

  def update_data
    data_editor = DataEditor.find_by_name('test13')
    data = params[:data]
    row_id = data[:rowid].to_i
    col_id = data[:icol].to_i
    row = data_editor.data[:rows][row_id]
    row[:cell][col_id] = data[:value]
    data_editor.data_will_change!
    data_editor.save!
    render :json => {}.to_json
  end
  
  def data
    unless data_editor = DataEditor.find_by_name('test13')
      data_editor = DataEditor.new
      data_editor.data = {:rows => DataEditor.testdata}
      data_editor.name = 'test13'
      data_editor.save!
    end
    
    data = {:page => 1, :records => data_editor.data[:rows].size, :rows => data_editor.data[:rows], :total => 1}
    render :json => data.to_json
  end
  
  def index
    
  end

end
