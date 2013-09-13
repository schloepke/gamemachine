Admin::Application.routes.draw do
  devise_for :users
  get '/admin/server/:id/start', to: 'server#start'
  get '/admin/server/:id/stop', to: 'server#stop'
  mount RailsAdmin::Engine => '/admin', :as => 'rails_admin'
end
