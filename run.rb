Thread.abort_on_exception = true
$LOAD_PATH.unshift('lib',(__FILE__))
require 'game_machine'

GameMachine::Server::SocketServer.run
puts 'socket server started'
GameMachine::Server::HttpServer.run
puts 'http server started'
