require 'rubygems'
path = File.expand_path(File.join(File.dirname(__FILE__), './lib'))
$LOAD_PATH << path

ENV['GAME_ENV'] = 'development'
require 'game_machine'


GameMachine::Server.start
sleep 2
GameMachine::Server.stop
#GameMachine::Client.client3
