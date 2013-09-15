Admin::Application.routes.draw do
  devise_for :users
  get '/admin/server/:id/start', to: 'server#start'
  get '/admin/server/:id/stop', to: 'server#stop'
  mount RailsAdmin::Engine => '/admin', :as => 'rails_admin'
  get '/system/restart', to: 'system#restart'
  get '/system/publish', to: 'system#publish'
  get '/system/recompile', to: 'system#recompile'
end
