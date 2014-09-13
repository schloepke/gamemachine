require 'json'
require 'sinatra/base'
require 'rack-flash'


class WebApp < Sinatra::Base
  set :bind, GameMachine::Application.config.http.host
  set :port, GameMachine::Application.config.http.port
  set :root, File.expand_path(File.dirname(__FILE__))
  set :environment, :development
  mime_type :proto, 'application/octet-stream'
  set :server, :puma

  enable :sessions
  set :session_secret, 'dsf5785sadf86876asdf6'
  use Rack::Flash

  helpers do
    def logged_in?
      session['user']
    end

    def admin_user
      GameMachine::Application.config.admin.user
    end

    def admin_pass
      GameMachine::Application.config.admin.pass
    end

  end

  before do
    public = ['/auth','/login','/clusterinfo']
    unless public.include?(request.path_info)
      unless logged_in?
        redirect to('/login')
      end
    end
  end

  get '/' do
    erb :index
  end

  get '/login' do
    erb :login
  end

  post '/login' do
    if params['username'] == admin_user && params['password'] == admin_pass
      flash[:notice] = "Logged in"
      session['user'] = true
      redirect to('/')
    else
      flash[:error] = "Invalid username or password"
      redirect to("/login")
    end
  end

  get '/add_player' do
    erb :add_player
  end

  post '/add_player' do
    if player = GameMachine::EntityLib::PlayerEntity.create(params['username'],params['password'])
      flash[:notice] = "Player created"
    else
      flash[:error] = "Error creating player (already exists?)"
    end
    redirect to('/')
  end

  get '/clusterinfo' do
    info = {}
    info[:members] = {}
    GameMachine::ClusterMonitor.cluster_members.keys.each do |key|
      member = GameMachine::ClusterMonitor.cluster_members[key]
      info[:members][key] = {:address => member.address, :status => member.status}
    end
    info[:self_address] = GameMachine::Akka.instance.address

    JSON.generate(info)
  end

  post '/auth' do
    if authtoken = GameMachine::Application.auth_handler.authorize(params['username'],params['password'])
      authtoken
    else
      status 403
      'error'
    end
  end

end

WebApp.run!
