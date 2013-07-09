require 'rubygems'
path = File.expand_path(File.join(File.dirname(__FILE__), './lib'))
$LOAD_PATH << path
ENV['GAME_ENV'] = 'development'
require 'game_machine'

