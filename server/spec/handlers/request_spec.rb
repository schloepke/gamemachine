require 'spec_helper'

module GameMachine
  module Handlers
    describe Request do

      let(:player) {MessageLib::Player.new.set_id('1')}
      let(:client_id) {'client1'}
      let(:client_connection) {MessageLib::ClientConnection.new}

      let(:entity) do
       MessageLib::Entity.new.set_id('1')
      end

      let(:player_logout) do
        MessageLib::PlayerLogout.new.set_player_id(player.id)
      end

      let(:client_message) do
        MessageLib::ClientMessage.new.
          set_client_connection(client_connection).
          set_player(player).
          add_entity(entity)
      end

      let(:actor_ref) {double('Actor::Ref', :tell => true)}

      describe "#on_receive" do

        before(:each) do
          Actor::Builder.new(Request).with_name('test_request_handler').start
        end

        it "sets player on entities" do
          message = client_message
          entity = message.get_entity_list.first
          entity.should_receive(:set_player).with(message.player)
          Request.should_receive_message(message,'test_request_handler') do
            Request.find('test_request_handler').tell(message)
          end
        end


        it "calls unhandled if not a client message" do
          Request.any_instance.should_receive(:unhandled).with('test')
          Request.should_receive_message('test','test_request_handler') do
            Request.find('test_request_handler').tell('test')
          end
        end

        it "calls unhandled if player is not set" do
          message = client_message
          message.set_player(nil)
          Request.any_instance.should_receive(:unhandled).with(message)
          Request.should_receive_message(message,'test_request_handler') do
            Request.find('test_request_handler').tell(message)
          end
        end

      end


    end
  end
end
