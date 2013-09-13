class ServerController < ApplicationController

  def stop
    server = Server.find(params[:id])
    server.stop
    flash[:notice] = "Server stopping"
    redirect_to :back
  end

  def start
    server = Server.find(params[:id])
    server.start
    flash[:notice] = "Server started"
    redirect_to :back
  end

  def index
    if request.headers['X-PJAX']
    render :layout => false
    else
      redirect_to '/admin'
    end
  end
end
