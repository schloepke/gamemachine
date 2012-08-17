require 'base64'
class MainController < ApplicationController

  def poll
    if params[:updates]
      params[:updates].values.each do |value|
        DataEditor.add_update(params[:user_id],value)
      end
    end
    
    if DataEditor.user_updates[params[:user_id]].empty?
      updates = nil
    else
      updates = DataEditor.user_updates[params[:user_id]].dup
      DataEditor.user_updates[params[:user_id]] = []
    end
    
    puts updates.inspect
    render :json => {:updates => updates}.to_json
  end
  
  def update_data
    data_editor = DataEditor.find_by_name(DataEditor.current_name)
    user_id = params[:data][:user_id]
    data = data_editor.deserialize_data
    row_id = params[:data][:rowid].to_i
    col_id = params[:data][:icol].to_i - 1
    row = data[:rows][row_id]
    row[:cell][col_id] = params[:data][:value]
    data_editor.serialize_data(data)
    data_editor.data_will_change!
    data_editor.save!
    render :json => {}.to_json
  end
  
  def data
    unless data_editor = DataEditor.find_by_name(DataEditor.current_name)
      data_editor = DataEditor.new

      data_editor.serialize_data({:rows => DataEditor.testdata})
      data_editor.name = DataEditor.current_name
      data_editor.save!
    end
    data = data_editor.deserialize_data
    data = {:page => 1, :records => data[:rows].size, :rows => data[:rows], :total => 1}
    render :json => data.to_json
  end
  
  def index
    DataEditor.user_updates[params[:user_id]] ||= []
  end

end
