require 'spec_helper'

module GameMachine
  module Navigation
    describe DetourNavmesh do

      let(:meshfile) {File.join(GameMachine.app_root,'../data/meshes/test.bin')}
      let(:bad_meshfile) {File.join(GameMachine.app_root,'../data/meshes/bad.bin')}
      let(:navmesh_id) {1}

      describe "#create" do
        it "raises an error if meshfile does not exist" do
          expect {
            DetourNavmesh.create(navmesh_id,bad_meshfile)
          }.to raise_error(/file does not exist/)
        end
      end

      describe "#load_mesh!" do
        it "should return 1 on success" do
          expect(DetourNavmesh.create(navmesh_id,meshfile).load_mesh!).to eql(1)
        end

        it "should raise error on failure" do
          #Detour.stub(:loadNavMesh).and_return(0)
          expect {
            DetourNavmesh.new(navmesh_id,meshfile).load_mesh!
          }.to raise_error(/failed to load/)
        end
      end

    end
  end
end
