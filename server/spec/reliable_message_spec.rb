require 'spec_helper'
module GameMachine

  describe "Reliable Messages" do
    let(:entity_id) {'1'}
    let(:entity) do
      MessageLib::Entity.new.set_id(entity_id).
      set_player(MessageLib::Player.new.set_id('player')).
      set_reliable(1)
    end

    subject do
      ref = Actor::Builder.new(GameSystems::Devnull).with_name('devnull').test_ref
      ref.underlying_actor
    end

    before(:each) {JavaLib::GameActor.remove_reliable_message(entity_id)}

    describe "#exactly_once" do
      it "should return true exactly once" do
        expect(subject.exactly_once(entity)).to be_truthy
        expect(subject.exactly_once(entity)).to be_falsy
      end
    end

    describe "#confirm_delivery" do
      it "should return false if message not yet delivered" do
        expect(subject.confirm_delivery(entity)).to be_falsy
      end

      it "should return true if message delivered" do
        subject.exactly_once(entity)
        expect(subject.confirm_delivery(entity)).to be_truthy
      end
    end
  end
end