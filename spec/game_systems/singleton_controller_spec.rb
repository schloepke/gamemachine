require 'spec_helper'

module GameMachine
  module GameSystems
    describe SingletonController do

      let(:entity) do
        Entity.new.set_id('entity')
      end

      let(:actor_ref) {double('Actor::Ref', :tell => true)}

      subject do
        props = JavaLib::Props.new(SingletonController);
        ref = JavaLib::TestActorRef.create(Akka.instance.actor_system, props, 'singleton_controller_test');
        ref.underlying_actor.post_init(*[entity])
        ref.underlying_actor
      end

      describe "#post_init" do

        it "sets the entity" do
          expect(subject.entity).to eq(entity)
        end

        it "sets the position to zero" do
          expect(subject.position.zero?).to be_true
        end

        it "calls start" do
          expect_any_instance_of(SingletonController).to receive(:start)
          subject
        end
      end

    end
  end
end
