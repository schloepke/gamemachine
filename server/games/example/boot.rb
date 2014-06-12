
require_relative 'lib/game'

game_root = File.dirname(__FILE__)
Example::Game.new(game_root).start

