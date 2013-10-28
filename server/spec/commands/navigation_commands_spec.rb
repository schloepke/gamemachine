require 'spec_helper'

module GameMachine
  module Commands
    describe NavigationCommands do

      let(:navmesh) {double("DetourNavmesh", :load_mesh! => true)}
      let(:query_ref) {double("DetourPath")}

      let(:meshfile) {File.join(GameMachine.app_root,'../data/meshes/test.bin')}
      subject{NavigationCommands.new}

      before(:each) do
        Navigation::DetourPath.stub(:new).and_return(query_ref)
        Navigation::DetourNavmesh.stub(:create).and_return(navmesh)
        Navigation::DetourNavmesh.stub(:find).and_return(navmesh)
      end

      describe "#load_navmesh" do
        it "returns a navmesh instance" do
          expect(
            subject.load_navmesh(1,meshfile)
          ).to eql(navmesh)
        end

        it "calls load_mesh! on navmesh" do
          expect(navmesh).to receive(:load_mesh!)
          subject.load_navmesh(1,meshfile)
        end

        it "raises an exception of navmesh file does not exist" do
          expect {
            subject.load_navmesh(1,'/blahblah')
          }.to raise_error(/does not exist/)
        end
      end

      describe "#navmesh" do
        it "returns the navmesh by id" do
          expect(subject.navmesh(1)).to eql(navmesh)
        end
      end

      describe "#query_ref" do
        it "returns a navmesh query reference" do
          expect(subject.query_ref(navmesh)).to eql(query_ref)
        end
      end
    end
  end
end
