require 'spec_helper'

module GameMachine
  module Navigation
    describe DetourPath do

      let(:meshfile) {File.join(GameMachine.app_root,'../data/meshes/test.bin')}

      subject {DetourPath}

      describe "#query_ref" do
        it "returns a detour path for the given navmesh id" do
          DetourNavmesh.create(2,meshfile)
          expect(subject.query_ref(1)).to be_a(subject)
        end

        it "raises an error if the navmesh is not found" do
          expect {
            subject.query_ref(3)
          }.to raise_error(/Navmesh with id 3 not found/)
        end
      end
    end
  end
end
