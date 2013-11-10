Admin2::Application.routes.draw do
  devise_for :users
  resources :app_servers

  get 'manage' => 'manage#index'

end
