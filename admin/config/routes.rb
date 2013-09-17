Admin::Application.routes.draw do
  devise_for :users
  get '/admin/server/:id/start', to: 'server#start'
  get '/admin/server/:id/stop', to: 'server#stop'
  mount RailsAdmin::Engine => '/admin', :as => 'rails_admin'


  get '/system/restart_game_server', to: 'system#restart_game_server'
  get '/system/start_game_server', to: 'system#start_game_server'
  get '/system/stop_game_server', to: 'system#stop_game_server'
  get '/system/publish', to: 'system#publish'
  get '/system/recompile', to: 'system#recompile'
  get '/system/restart', to: 'system#restart'
  get '/system/test', to: 'system#test'
  get '/system/action_required', to: 'system#action_required'
  get '/system/clear_status', to: 'system#clear_status'
end
