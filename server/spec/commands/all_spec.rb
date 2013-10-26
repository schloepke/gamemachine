require 'spec_helper'

module GameMachine
  module Commands
    describe All do

      let(:actor_ref) {double('Actor::Ref', :tell => true)}
      subject{All.new}

      describe "#send_to_player" do
        before(:each) do
          PlayerGateway.stub(:find).and_return(actor_ref)
        end

        it "sends component to player wrapped in entity" do
          expect(actor_ref).to receive(:tell).with(kind_of(Entity))
          subject.send_to_player(Attack.new,'1')
        end

        it "sends entity to player" do
          expect(actor_ref).to receive(:tell).with(kind_of(Entity))
          subject.send_to_player(Entity.new.set_id('1'),'1')
        end

        it "raises exception with invalid message" do
          expect {
            subject.send_to_player('blah','1')
          }.to raise_error(/not a valid object/)
        end
      end
    end
  end
end
