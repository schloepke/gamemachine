require 'spec_helper'

module GameMachine
  module Handlers
    
    describe Game do

      let(:player) {MessageLib::Player.new}
      let(:client_connection) {MessageLib::ClientConnection.new}

      let(:leave_chat) do
        MessageLib::Entity.new.set_leave_chat(MessageLib::LeaveChat.new).
          set_player(player).set_client_connection(client_connection)
      end

      let(:bad_entity) {MessageLib::Entity.new.set_id('1').set_publish(MessageLib::Publish.new)}
      let(:empty_entity) {MessageLib::Entity.new.set_id('1')}

      let(:actor_ref) {double('DispatcherActor::Ref')}

      describe "aspects" do
        before(:each) do
          Actor::Builder.new(Game).with_name('dispatch_test').start
        end

        it "dispatches entities to correct system" do
          client_message = MessageLib::ClientMessage.new.add_entity(leave_chat)
          GameSystems::ChatManager.stub(:find).and_return(actor_ref)
          actor_ref.should_receive(:tell)
          Game.should_receive_message(client_message,'dispatch_test') do
            Game.find('dispatch_test').tell(client_message)
          end
        end

        it "does not dispatch when aspect not found" do
          client_message = MessageLib::ClientMessage.new.add_entity(empty_entity)
          Game.should_receive_message(client_message,'dispatch_test') do
            Game.find('dispatch_test').tell(client_message)
          end
        end

      end

    end

  end
end


