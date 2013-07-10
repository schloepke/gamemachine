
require 'rubygems'
ENV['BUNDLE_GEMFILE'] ||= File.expand_path('../../Gemfile', __FILE__)
require 'bundler/setup' if File.exists?(ENV['BUNDLE_GEMFILE'])
Bundler.setup(:default)

require 'slop'
begin
  require "game_machine"
rescue LoadError
  require_relative "../lib/game_machine"
end

opts = Slop.parse(:help => true) do
  banner 'Usage: datacube [options]'

  on '--cluster',  'start in cluster mode'
  on 'name=',  'Server name'
  on '--server',  'start in standalone server mode'
  on '--stop',  'stop all nodes'

  help
end


if opts.stop?
  GameMachine::Server.instance.init!
  GameMachine::Server.instance.kill_all
end

ENV['APP_ROOT'] ||= File.join(Dir.getwd, '/app')

server_opts = {}

if opts.cluster?
  server_opts[:cluster] = true
end

if opts.server? || opts.cluster?
  GameMachine::Server.instance.init!(
    (opts[:name] || 'default'), server_opts
  )
  GameMachine::Server.instance.start
end
