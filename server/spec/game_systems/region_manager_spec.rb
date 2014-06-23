require 'spec_helper'

module GameMachine
  module GameSystems

    class Zone2Manager < Actor::Base
      def on_receive(message);end
    end
    class Zone1Manager < Actor::Base
      def on_receive(message);end
    end

    describe RegionManager do

      let(:zone1_manager) {'GameMachine::GameSystems::Zone1Manager'}
      let(:zone2_manager) {'GameMachine::GameSystems::Zone2Manager'}

      let(:region1) {double('region1', :name => 'zone1', :server => nil,
                            :manager => zone1_manager, :save => true)}
      let(:region2) {double('region2', :name => 'zone2', :server => nil,
                            :manager => zone2_manager, :save => true)}

      let(:regions) {{'zone1' => region1, 'zone2' => region2}}

      let(:cluster_member) {double('cluster_member')}

      let(:server1_address) {'akka.tcp://cluster@localhost:2551'}
      let(:server2_address) {'akka.tcp://cluster@localhost:2552'}

      let(:cluster_members) do
        {server1_address => cluster_member, server2_address => cluster_member}
      end

      let(:cluster_with_down_members) do
        {server1_address => cluster_member}
      end

      let(:actor_ref) {double('Actor::Ref', :tell => true)}

      subject do
        ref = Actor::Builder.new(RegionManager).with_name('region_manager_test').test_ref
        ref.underlying_actor
      end

      describe "#assign_servers" do
        it "should assign servers to regions that have none" do
          subject.stub(:regions).and_return(regions)
          ClusterMonitor.stub(:cluster_members).and_return(cluster_members)
          expect(region1).to receive(:server=)
          expect(region2).to receive(:server=)
          expect(region1).to receive(:save)
          expect(region2).to receive(:save)
          subject.assign_servers
          expect(subject.servers[server1_address]).to eq region1.name
          expect(subject.servers[server2_address]).to eq region2.name
        end

      end

      describe "#notify_managers" do
        it "should send message to manager of each region" do
          expect(GameSystems::Zone1Manager).to receive(:find).
            and_return(actor_ref)
          expect(GameSystems::Zone2Manager).to receive(:find).
            and_return(actor_ref)
          subject.notify_managers
        end
      end

      describe "#load_from_config" do
        
        it "should create new regions that do not exist" do
          expect(Models::Region).to receive(:find).
            with('zone1',5000).and_return(nil)
          expect(Models::Region).to receive(:find).
            with('zone2',5000).and_return(nil)

          expect(Models::Region).to receive(:new).
            with(:name => 'zone1', :manager => zone1_manager).and_return(region1)
          expect(Models::Region).to receive(:new).
            with(:name => 'zone2', :manager => zone2_manager).and_return(region2)

          expect(region1).to receive(:save)
          expect(region2).to receive(:save)
          expect(subject.regions['zone1']).to eq region1
          expect(subject.regions['zone2']).to eq region2
        end
        
        it "should load regions that exist" do
          expect(Models::Region).to receive(:find).with('zone1',5000).and_return(region1)
          expect(Models::Region).to receive(:find).with('zone2',5000).and_return(region2)
          expect(subject.regions['zone1']).to eq region1
          expect(subject.regions['zone2']).to eq region2
        end
      end

      describe "#unassign_down_servers" do

        context "nodes have not changed" do
          it "should not unassign servers from regions" do
            ClusterMonitor.stub(:cluster_members).and_return(cluster_members)
            subject.unassign_down_servers
            expect(region1).to_not receive(:save)
            expect(region2).to_not receive(:save)
          end
        end

        context "node has been downed" do

          before(:each) do
            subject.regions['zone1'].server = server2_address
            subject.servers[server2_address] = 'zone1'
            ClusterMonitor.stub(:cluster_members).and_return(cluster_with_down_members)
          end

          it "should set region server to nil" do
            subject.unassign_down_servers
            expect(subject.regions['zone1'].server).to be_nil
          end

          it "should remove servers entry" do
            subject.unassign_down_servers
            expect(subject.servers.has_key?(server2_address)).to be_false
          end

          it "should save region" do
            expect(subject.regions['zone1']).to receive(:save)
            subject.unassign_down_servers
          end
        end
      end

      describe "#server_hsotname" do
        it "should parse akka address to hostname" do
          expect(subject.server_hostname(server1_address)).to eq 'localhost'
        end
      end

      describe "#regions_string" do
        it "should return correctly formatted string" do
          subject.stub(:regions).and_return(regions)
          region1.stub(:server).and_return(server1_address)
          region2.stub(:server).and_return(server2_address)
          expected = "zone1=localhost|zone2=localhost"
          expect(subject.regions_string).to eq expected
        end

        it "should return empty string with no servers assigned for regions" do
          expect(subject.regions_string).to eq ''
        end
      end
    end
  end
end
