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

      let(:get_neighbors) do
        GetNeighbors.new.set_neighbor_type('player').set_vector3(vector3)
      end

      let(:player) do
        Player.new.set_id('1')
      end

      let(:entity) do
        Entity.new.set_id('0').
          set_player(player).
          set_track_entity(track_entity).
          set_entity_type('player').
          set_vector3(vector3)
      end

      let(:get_neighbors_entity) do
        Entity.new.set_id('0').set_get_neighbors(get_neighbors)
      end

      let(:actor_ref) {double('Actor::Ref', :tell => true)}

      describe 'tracking location' do

        it "should call grid.set with player" do
          expect(subject.grid).to receive(:set).with('0',0.0,0.0,0.0,'player')
          subject.on_receive(entity)
        end

      end

      describe 'get neighbors' do
        it "sends neighbors to player" do
          expect(subject).to receive(:send_neighbors_to_player)
          get_neighbors_entity.set_player(player)
          subject.on_receive(entity)
          subject.on_receive(get_neighbors_entity)
        end
      end

    end
  end
end
