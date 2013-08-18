require 'spec_helper'

module GameMachine
  module GameSystems
    describe EntityTracking do


      subject do
        props = JavaLib::Props.new(EntityTracking);
        ref = JavaLib::TestActorRef.create(Akka.instance.actor_system, props, 'entity_tracking_test');
        ref.underlying_actor.post_init
        ref.underlying_actor
      end

      let(:track_entity) do
        TrackEntity.new.set_value(true)
      end

      let(:vector3) do
        Vector3.new.set_x(0).set_y(0).set_z(0)
      end

      let(:transform) do
        Transform.new.set_vector3(vector3)
      end

      let(:get_neighbors) do
        GetNeighbors.new.set_value(true).set_vector3(vector3)
      end

      let(:player) do
        Player.new.set_id('1').set_transform(transform)
      end

      let(:npc) do
        Npc.new.set_id('1').set_transform(transform)
      end

      let(:entity) do
        Entity.new.set_id('0').
          set_player(player).
          set_track_entity(track_entity)
      end

      let(:npc_entity) do
        Entity.new.set_id('0').
          set_npc(npc).
          set_track_entity(track_entity)
      end

      let(:get_neighbors_entity) do
        Entity.new.set_id('0').set_get_neighbors(get_neighbors)
      end

      let(:actor_ref) {double('Actor::Ref', :tell => true)}

      describe 'tracking location' do

        it "should call grid.set with player" do
          expect(subject.grid).to receive(:set).with(entity.player)
          subject.on_receive(entity)
        end

        it "should call grid.set with npc" do
          expect(subject.grid).to receive(:set).with(npc_entity.npc)
          subject.on_receive(npc_entity)
        end

        it "publishes the update to the message queue" do
          MessageQueue.stub(:find).and_return(actor_ref)
          expect(actor_ref).to receive(:tell).with(kind_of(Publish),subject)
          subject.on_receive(entity)
        end

        it "does not publish when message is from other entity trackers" do
          expect(subject).to_not receive(:publish_entity_location_update)
          entity.track_entity.set_internal(true)
          subject.on_receive(entity)
        end

      end

      describe 'get neighbors' do

        it "gets neighbors from grid" do
          expect(subject.grid).to receive(:neighbors).and_return([[0,1,player]])
          subject.on_receive(get_neighbors_entity)
        end

        it "sends neighbors to player" do
          expect_any_instance_of(ClientMessage).to receive(:send_to_player)
          get_neighbors_entity.set_player(player)
          subject.on_receive(entity)
          subject.on_receive(get_neighbors_entity)
        end
      end

    end
  end
end
