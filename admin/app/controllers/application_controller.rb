class ApplicationController < ActionController::Base
  # Prevent CSRF attacks by raising an exception.
  # For APIs, you may want to use :null_session instead.
  protect_from_forgery with: :exception
  #before_filter :check_system_status

  protected

  def check_system_status
    if SystemStatus.status.action_required?
      #redirect_to '/system/action_required'
    end
  end
end
