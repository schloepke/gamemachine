require 'spec_helper'
module GameMachine
  module Navigation
    describe DetourPath do
      let(:meshfile) do
        "/home/chris/game_machine/server/detour/test2.bin"
      end

      it "create and load navmesh" do
        navmesh = DetourNavmesh.create(1,meshfile)
        expect(navmesh.load_mesh!).to eq(1)
      end

      it "finds a path" do
        navmesh = DetourNavmesh.create(2,meshfile)
        expect(navmesh.load_mesh!).to eq(1)
        pathfinder = DetourPath.new(navmesh)
        path = pathfinder.find_path(10.0,0,10.0,109.0,0,109.0,10,0.5)
        expect(pathfinder.error).to be_nil
        expect(path.size).to eq(10)
      end

      it "does not leak memory" do
        navmesh = DetourNavmesh.create(3,meshfile)
        navmesh.load_mesh!
        threads = []
        4.times do
          threads << Thread.new do
            pathfinder = DetourPath.new(navmesh)
            10.times do
              path = pathfinder.find_path(10.0,0,10.0,109.0,0,109.0,10,0.7)
              expect(pathfinder.error).to be_nil
            end
          end
        end
        threads.map(&:join)
      end
    end
  end
end
