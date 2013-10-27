require 'spec_helper'

module GameMachine
  module Commands
    describe ChatCommands do

      let(:actor_ref) {double('Actor::Ref', :tell => true)}
      let(:chat_message) {double("chat_message")}
      let(:join_message) {double("join_message")}
      let(:leave_message) {double("leave_message")}

      subject{ChatCommands.new}

      before(:each) do
        GameSystems::Chat.stub(:find).and_return(actor_ref)
        subject.stub(:chat_message).and_return(chat_message)
        subject.stub(:join_message).and_return(join_message)
        subject.stub(:leave_message).and_return(leave_message)
      end

      describe "#send_private_message" do
        it "sends a private message request to the chat actor" do
          expect(actor_ref).to receive(:tell).with(chat_message)
          subject.send_private_message('1','blah')
        end
      end

      describe "#join" do
        it "joins a chat group" do
          expect(actor_ref).to receive(:tell).with(join_message)
          subject.join('blah')
        end
      end

      describe "#leave" do
        it "leaves a chat group" do
          expect(actor_ref).to receive(:tell).with(leave_message)
          subject.leave('blah')
        end
      end

    end
  end
end
