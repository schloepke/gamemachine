Admin::Application.routes.draw do
  devise_for :users
  get '/admin/server', to: 'server#index'
  mount RailsAdmin::Engine => '/admin', :as => 'rails_admin'
end
