require 'spec_helper'

module GameMachine
  module GameSystems
    describe SingletonController do

      let(:entity) do
        MessageLib::Entity.new.set_id('entity')
      end

      let(:actor_ref) {double('Actor::Ref', :tell => true)}

      let(:singleton2) do
        Actor::Builder.new(SingletonController).
          with_name('singleton_controller_test2').test_ref
      end

      subject do
        ref = Actor::Builder.new(SingletonController).with_name('singleton_controller_test').test_ref
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
          expect(singleton2.underlying_actor).to receive(:start)
          singleton2.underlying_actor.post_init(*[entity])
        end
      end

    end
  end
end
