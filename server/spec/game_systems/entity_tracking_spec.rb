require 'spec_helper'

module GameMachine
  module GameSystems
    describe EntityTracking do


      subject do
        ref = Actor::Builder.new(EntityTracking).with_name('entity_tracking_test').test_ref
        ref.underlying_actor.post_init
        ref.underlying_actor
      end

      let(:track_entity) do
        MessageLib::TrackEntity.new.set_value(true)
      end

      let(:vector3) do
        MessageLib::Vector3.new.set_x(0).set_y(0).set_z(0)
      end

      let(:get_neighbors) do
        MessageLib::GetNeighbors.new.set_neighbor_type('player').set_vector3(vector3)
      end

      let(:player) do
        MessageLib::Player.new.set_id('1')
      end

      let(:entity) do
        MessageLib::Entity.new.set_id('0').
          set_player(player).
          set_track_entity(track_entity).
          set_entity_type('player').
          set_vector3(vector3)
      end

      let(:get_neighbors_entity) do
        MessageLib::Entity.new.set_id('0').set_get_neighbors(get_neighbors)
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
