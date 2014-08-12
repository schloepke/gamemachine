require 'spec_helper'
module GameMachine

  describe "Reliable Messages" do
    let(:id) {'1'}
    let(:game_message) do
      MessageLib::GameMessage.new.set_message_id(id)
    end

    subject do
      ref = Actor::Builder.new(GameSystems::Devnull).with_name('devnull').test_ref
      ref.underlying_actor
    end

    before(:each) do
      JavaLib::GameActor.remove_reliable_message(id)
      subject.set_player_id('player')
    end

    describe "#exactly_once" do
      it "should return true exactly once" do
        expect(subject.exactly_once(game_message)).to be_truthy
        expect(subject.exactly_once(game_message)).to be_falsy
      end
    end

    describe "#set_reply" do
      it "should return false if message not yet delivered" do
        expect(subject.set_reply(game_message)).to be_falsy
      end

      it "should return true if message delivered" do
        subject.exactly_once(game_message)
        expect(subject.set_reply(game_message)).to be_truthy
      end
    end
  end
end