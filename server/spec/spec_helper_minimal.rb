ENV['APP_ROOT'] ||= File.expand_path(Dir.pwd)
ENV['JAVA_ROOT'] = File.join(ENV['APP_ROOT'],'java','project')
ENV['GAME_ENV'] = 'test'
require 'rubygems'

begin
  require 'game_machine'
rescue LoadError
  require_relative '../lib/game_machine'
end