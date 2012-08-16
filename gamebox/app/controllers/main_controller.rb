require 'base64'
class MainController < ApplicationController

  def update_data
    data_editor = DataEditor.find_by_name('test13')

    data = data_editor.deserialize_data
    row_id = params[:data][:rowid].to_i
    col_id = params[:data][:icol].to_i
    row = data[:rows][row_id]
    row[:cell][col_id] = params[:data][:value]
    data_editor.serialize_data(data)
    data_editor.data_will_change!
    data_editor.save!
    render :json => {}.to_json
  end
  
  def data
    unless data_editor = DataEditor.find_by_name('test13')
      data_editor = DataEditor.new

      data_editor.serialize_data({:rows => DataEditor.testdata})
      data_editor.name = 'test13'
      data_editor.save!
    end
    data = data_editor.deserialize_data
    data = {:page => 1, :records => data[:rows].size, :rows => data[:rows], :total => 1}
    render :json => data.to_json
  end
  
  def index
    
  end

end
