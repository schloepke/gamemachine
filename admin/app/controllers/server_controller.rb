class ServerController < ApplicationController

  def index
    if request.headers['X-PJAX']
    render :layout => false
    else
      redirect_to '/admin'
    end
  end
end
