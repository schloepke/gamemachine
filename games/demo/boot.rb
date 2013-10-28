
require_relative 'lib/game'

game_root = File.dirname(__FILE__)

meshfile = File.join(game_root,'data','meshes',"mesh1.bin")
if File.exists?(meshfile)
  navmesh = GameMachine::Navigation::DetourNavmesh.create(1,meshfile)
  navmesh.load_mesh!
else
  raise "Meshfile #{meshfile} does not exist"
end
Demo::Game.new(game_root).start

