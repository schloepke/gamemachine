require 'rails_admin/config/actions'
require 'rails_admin/config/actions/base'
 
module RailsAdmin
  module Config
    module Actions
      class StartServer < RailsAdmin::Config::Actions::Base
        register_instance_option :visible? do
          bindings[:object].is_a?(Server) &&
            bindings[:object].status == 'Not running'
        end

        register_instance_option :member? do
          true
        end

        register_instance_option :link_icon do
          'icon-check'
        end

        register_instance_option :controller do
          Proc.new do
            @object.update_attribute(:status, 'Starting')
            flash[:notice] = "Server starting"
        
            redirect_to back_or_index
          end
        end

        RailsAdmin::Config::Actions.register(self)
      end
    end
  end
end
