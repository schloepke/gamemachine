require 'spec_helper'

module GameMachine
  module Commands

    describe PlayerCommands do

      let(:entity) do
        entity = MessageLib::Entity.new
        entity.set_id('1')
        entity
      end

      let(:component) do
        MessageLib::Attack.new
      end

      let(:actor_ref) {double('Actor::Ref', :tell => true)}

      subject{PlayerCommands.new}

      describe "player" do
        describe "#send_message" do
          before(:each) do
            allow(ClientManager).to receive(:find).and_return(actor_ref)
          end

          it "sends component to player wrapped in entity" do
            expect(actor_ref).to receive(:tell).with(kind_of(MessageLib::Entity))
            subject.send_message(component,'1')
          end

          it "sends entity to player" do
            expect(actor_ref).to receive(:tell).with(kind_of(MessageLib::Entity))
            subject.send_message(MessageLib::Entity.new.set_id('1'),'1')
          end

          it "raises exception with invalid message" do
            expect {
              subject.send_message('blah','1')
            }.to raise_error(/not a valid object/)
          end
        end
      end

    end
  end
end
