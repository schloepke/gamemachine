require_relative 'lib/demo'

meshfile = File.join(GameMachine.app_root,'../data','meshes',"mesh1.bin")
if File.exists?(meshfile)
  navmesh = GameMachine::Navigation::DetourNavmesh.create(1,meshfile)
  navmesh.load_mesh!
else
  raise "Meshfile #{meshfile} does not exist"
end
Demo::Game.new.start
