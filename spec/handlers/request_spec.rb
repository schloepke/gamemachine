require 'spec_helper'

module GameMachine
  module Handlers
    describe Request do

      let(:player) {Player.new.set_id('1')}
      let(:client_id) {'client1'}
      let(:client_connection) {ClientConnection.new}

      let(:entity) do
       Entity.new.set_id('1')
      end

      let(:client_disconnect) do
        ClientDisconnect.new.set_client_connection(
          ClientConnection.new.set_id(client_id).set_gateway('blah')
        )
      end

      let(:player_logout) do
        PlayerLogout.new.set_player_id(player.id)
      end

      let(:client_message) do
        ClientMessage.new.
          set_client_connection(client_connection).
          set_player(player).
          add_entity(entity)
      end

      let(:actor_ref) {double('Actor::Ref', :tell => true)}

      describe "#on_receive" do

        before(:each) do
          Handlers::Authentication.stub(:find_distributed_local).and_return(actor_ref)
          Actor::Builder.new(Request).with_name('test_request_handler').start
        end

        it "logs out player if PlayerLogout present" do
          message = client_message
          message.set_player_logout(player_logout)
          expect(PlayerRegistry).to receive(:player_logout).with(
            message.player_logout.player_id
          )
          Request.should_receive_message(message,'test_request_handler') do
            Request.find('test_request_handler').tell(message)
          end
        end

        it "disconnects client if ClientDisconnect present" do
          message = client_message
          message.set_client_disconnect(client_disconnect)
          expect(PlayerRegistry).to receive(:client_disconnect).with(
            message.client_disconnect.client_connection
          )
          Request.should_receive_message(message,'test_request_handler') do
            Request.find('test_request_handler').tell(message)
          end
        end

        it "sets player on entities" do
          message = client_message
          entity = message.get_entity_list.first
          entity.should_receive(:set_player).with(message.player)
          Request.should_receive_message(message,'test_request_handler') do
            Request.find('test_request_handler').tell(message)
          end
        end

        it "forwards message to authentication handler" do
          message = client_message
          actor_ref.should_receive(:tell).with(message,anything())
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
