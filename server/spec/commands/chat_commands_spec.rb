require 'spec_helper'

module GameMachine
  module Commands
    describe ChatCommands do

      let(:actor_ref) {double('Actor::Ref', :tell => true)}

      subject{ChatCommands.new}

      before(:each) do
        allow(GameSystems::ChatManager).to receive(:find).and_return(actor_ref)
      end

      describe "#send_private_message" do
        it "sends a private message request to the chat actor" do
          expect(actor_ref).to receive(:tell)
          subject.send_private_message('1','blah')
        end
      end

      describe "#join" do
        it "joins a chat group" do
          expect(actor_ref).to receive(:tell)
          subject.join('blah','player1')
        end
      end

      describe "#leave" do
        it "leaves a chat group" do
          expect(actor_ref).to receive(:tell)
          subject.leave('blah','player1')
        end
      end

    end
  end
end
