ENV['BUNDLE_GEMFILE'] ||= File.expand_path('../../Gemfile', __FILE__)
ENV['APP_ROOT'] ||= File.join(File.dirname(__FILE__), '../')
ENV['GAME_ENV'] ||= 'development'
require 'rubygems'
require 'bundler/setup' if File.exists?(ENV['BUNDLE_GEMFILE'])
Bundler.setup(:default)


require 'slop'
begin
  require "game_machine"
rescue LoadError
  require_relative "../lib/game_machine"
end

module GameMachine
  class Application

    #config.handlers = [Handlers::Request,Handlers::Authentication, Handlers::Game]
  end
end

puts GameMachine::Application.config.handlers

opts = Slop.parse(:help => true) do
  banner 'Usage: datacube [options]'

  on '--cluster',  'start in cluster mode'
  on 'name=',  'Akka name'
  on '--server',  'start in standalone server mode'
  on '--stop',  'stop all nodes'

  help
end


if opts.stop?
  GameMachine::Akka.instance.init!
  GameMachine::Akka.instance.kill_all
end


if opts.server? || opts.cluster?
  GameMachine::Application.initialize!(opts[:name],opts.cluster?)
  GameMachine::Application.start
end
