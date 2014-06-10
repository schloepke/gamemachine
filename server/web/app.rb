require 'json'
require 'sinatra/base'
require 'sinatra/multi_route'

require_relative 'controllers/base_controller'
require_relative 'controllers/index_controller'
require_relative 'controllers/messages_controller'
require_relative 'controllers/auth_controller'
require_relative 'controllers/log_controller'


class WebApp < Sinatra::Base
  CONTROLLERS = {}
  set :bind, GameMachine::Application.config.http_host
  set :port, GameMachine::Application.config.http_port
  set :root, File.expand_path(File.dirname(__FILE__))
  set :environment, :production
  mime_type :proto, 'application/octet-stream'

  register Sinatra::MultiRoute

  def base_uri
    host =  GameMachine::Application.config.http_host
    port = GameMachine::Application.config.http_port
    "http://#{host}:#{port}"
  end

  def controller(name)
    case name
    when :index
      CONTROLLERS[name] ||= Web::Controllers::IndexController.new
    when :auth
      CONTROLLERS[name] ||= Web::Controllers::AuthController.new
    when :messages
      CONTROLLERS[name] ||= Web::Controllers::MessagesController.new
    when :log
      CONTROLLERS[name] ||= Web::Controllers::LogController.new
    end
  end

  get '/' do
    if request.params['restarted']
      @restarted = true
    end
    haml :index
  end

  get '/alive' do
    JSON.generate({})
  end

  get '/restart' do
    haml :restart
  end

  get '/logs' do
    @logtypes = {
      :app => 'Application',
      :stdout => 'Standard out',
      :stderr => 'Standard error'
    }
    @logtype = (params['logtype'] || 'app').to_sym
    @content = controller(:log).set_request(request,params).logs(@logtype)
    haml :logs
  end

  get '/messages/game' do
    @content = controller(:messages).set_request(request,params).game
    @messages = :game
    haml :game_messages
  end

  post '/messages/game' do
    @content = controller(:messages).set_request(request,params).update
    if @content == 'restart'
      haml :restart
    else
      @messages = :game
      haml :game_messages
    end
  end

  get '/messages/all' do
    @content = controller(:messages).set_request(request,params).all
    @messages = :all
    @format = params['format']
    if @format == 'proto'
      content_type :proto
      attachment 'messages.proto'
      @content
    else
      haml :game_messages
    end
  end

  route :get, :post, '/auth' do
    controller(:auth).set_request(request,params).get
  end
end

WebApp.run!

