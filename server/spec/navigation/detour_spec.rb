require 'spec_helper'
module GameMachine
  module Navigation
    describe DetourPath do
      let(:meshfile) do
        "/home2/chris/game_machine/server/detour/test2.bin"
      end

      let(:path_found) do
        [
          [10.0, 0.0, 10.0],
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
        path.each_with_index do |point,i|
          expected = Vector.from_array(path_found[i])
          expect(point==expected).to be_true
        end
      end

      it "does not leak memory" do
        navmesh = DetourNavmesh.create(3,meshfile)
        navmesh.load_mesh!
        threads = []
        4.times do
          threads << Thread.new do
            pathfinder = DetourPath.new(navmesh)
            100.times do
              path = pathfinder.find_path(10.0,0,10.0,109.0,0,109.0)
              expect(pathfinder.error).to be_nil
            end
          end
        end
        threads.map(&:join)
      end
    end
  end
end
