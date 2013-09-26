require 'spec_helper'
module GameMachine
  module Navigation
    describe "pathfinding" do
      let(:meshfile) do
        "/home2/chris/game_machine/server/detour/test2.bin"
      end

      let(:path_found) do
        [
          [105.5999984741211, 0.20000000298023224, 105.60000610351562],
          [109.0, 0.0, 109.0]
        ]
      end

      it "create and load navmesh" do
        navmesh = DetourNavmesh.create(1,meshfile)
        expect(navmesh.load_mesh!).to eq(1)
      end

      it "finds a path" do
        navmesh = DetourNavmesh.create(2,meshfile)
        expect(navmesh.load_mesh!).to eq(1)
        pathfinder = DetourPath.new(navmesh)
        path = pathfinder.find_path(10.0,0,10.0,109.0,0,109.0)
        expect(pathfinder.error).to be_nil
        expect(path).to eq(path_found)
      end
    end
  end
end
