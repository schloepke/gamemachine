require 'spec_helper'

module GameMachine
  module GameSystems
    describe SingletonController do

      let(:entity) do
        Entity.new.set_id('entity')
      end

      let(:parent) {double("Parent")}

      let(:actor_ref) {double('Actor::Ref', :tell => true)}

      subject {SingletonController.new(entity,parent)}

      describe "#new" do

        it "sets the entity" do
          expect(subject.entity).to eq(entity)
        end

        it "sets the parent" do
          expect(subject.parent).to eq(parent)
        end

        it "sets the position to zero" do
          expect(subject.position.zero?).to be_true
        end

        it "calls start" do
          expect_any_instance_of(SingletonController).to receive(:start)
          subject
        end
      end

      describe "#track" do
        it "sets the correct values on the tracking vector" do
          subject.position.x = subject.position.y = 5
          expect_any_instance_of(Vector3).to receive(:set_x).with(5).
            and_return(Vector3.new)
          expect_any_instance_of(Vector3).to receive(:set_y).with(5).
            and_return(Vector3.new)
          subject.track
        end

        it "should send tracking message to EntityTracking actor" do
          EntityTracking.stub(:find).and_return(actor_ref)
          actor_ref.stub(:actor).and_return(actor_ref)
          expect(actor_ref).to receive(:tell)
          subject.track
        end
      end

      describe "#neighbors" do
        it "should call neighbors_from_grid with correct position" do
          subject.position.x = subject.position.y = 5
          expect(EntityTracking).to receive(:neighbors_from_grid).
            with(5,5,nil,'player',nil)
          subject.neighbors
        end
      end
    end
  end
end
