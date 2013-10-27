require 'spec_helper'

module GameMachine
  class CommandTest
    include Commands
  end
  describe Commands do

    let(:actor_ref) {double('Actor::Ref', :tell => true)}

    subject do
      CommandTest.new
    end

    describe "grid" do
      describe "#find_by_id" do

      end

      describe "#neighbors" do

      end

      describe "#track" do

      end

    end

    describe "player" do
      describe "#send_message" do
        before(:each) do
          PlayerGateway.stub(:find).and_return(actor_ref)
        end

        it "sends component to player wrapped in entity" do
          expect(actor_ref).to receive(:tell).with(kind_of(MessageLib::Entity))
          subject.gm.player.send_message(MessageLib::Attack.new,'1')
        end

        it "sends entity to player" do
          expect(actor_ref).to receive(:tell).with(kind_of(MessageLib::Entity))
          subject.gm.player.send_message(MessageLib::Entity.new.set_id('1'),'1')
        end

        it "raises exception with invalid message" do
          expect {
            subject.gm.player.send_message('blah','1')
          }.to raise_error(/not a valid object/)
        end
      end
    end
  end
end
