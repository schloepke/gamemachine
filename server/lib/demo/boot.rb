require_relative 'lib/demo'

meshfile = "/home2/chris/game_machine/server/detour/all_tiles_navmesh.bin"
navmesh = GameMachine::Navigation::DetourNavmesh.create(1,meshfile)
navmesh.load_mesh!
Demo::Game.new.start
