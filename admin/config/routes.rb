Admin::Application.routes.draw do
  devise_for :users
  get '/admin/manage_server', to: 'server#index'
  mount RailsAdmin::Engine => '/admin', :as => 'rails_admin'
end
