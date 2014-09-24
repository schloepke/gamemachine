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

    def config
      GameMachine::Application.config
    end

    def logged_in?
      session['user']
    end

    def admin_user
      config.admin.user
    end

    def admin_pass
      config.admin.pass
    end

    def auth_response(token)
      response = {
        :authtoken => token,
        :protocol => config.client.protocol
      }

      if config.client.protocol == 'TCP'
        response['tcp_host'] = config.tcp.host
        response['tcp_port'] = config.tcp.port
      elsif config.client.protocol == 'UDP'
        response['udp_host'] = config.udp.host
        response['udp_port'] = config.udp.port
      end
      response
    end

  end

  before do
    allow = false
    public = ['/client','/login','/clusterinfo']
    public.each do |path|
      if request.path_info.match(path)
        allow = true
      end
    end

    if !allow && !logged_in?
      redirect to('/login')
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
    if player = GameMachine::MessageLib::Player.store_get('players',params['username'],2000)
      flash[:error] = "Error creating player (already exists?)"
    else
      player = GameMachine::MessageLib::Player.new.set_id(params['username'])
      authenticator = GameMachine::JavaLib::DefaultAuthenticator.new(player)
      authenticator.set_password(params['password'])
      flash[:notice] = "Player created"
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

  post '/api/client/login/:game_id' do
    if authtoken = GameMachine::Handlers::PlayerAuthentication.instance.authorize(params['username'],params['password'])
      content_type 'text/plain'
      JSON.generate(auth_response(authtoken))
    else
      status 403
      'error'
    end
  end

  get '/stats' do
    @stats = {}
    @stats[:local_player_count] = GameMachine::ClientManager.local_players.size
    @stats[:local_players] = GameMachine::ClientManager.local_players
  end
  
end

WebApp.run!
