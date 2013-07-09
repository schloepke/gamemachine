require 'spec_helper'

module GameMachine
  module Systems
    describe RequestHandler do

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

      let(:actor_ref) {double('ActorRef', :tell => true)}

      describe "#on_receive" do

        before(:each) do
          AuthenticationHandler.stub(:find_distributed_local).and_return(actor_ref)
          ActorBuilder.new(RequestHandler).with_name('test_request_handler').start
        end

        it "logs out player if PlayerLogout present" do
          message = client_message
          message.set_player_logout(player_logout)
          expect(PlayerRegistry).to receive(:player_logout).with(
            message.player_logout.player_id
          )
          RequestHandler.should_receive_message(message,'test_request_handler') do
            RequestHandler.find('test_request_handler').tell(message)
          end
        end

        it "disconnects client if ClientDisconnect present" do
          message = client_message
          message.set_client_disconnect(client_disconnect)
          expect(PlayerRegistry).to receive(:client_disconnect).with(
            message.client_disconnect.client_connection
          )
          RequestHandler.should_receive_message(message,'test_request_handler') do
            RequestHandler.find('test_request_handler').tell(message)
          end
        end

        it "sets player on entities" do
          message = client_message
          entity = message.get_entity_list.first
          entity.should_receive(:set_player).with(message.player)
          RequestHandler.should_receive_message(message,'test_request_handler') do
            RequestHandler.find('test_request_handler').tell(message)
          end
        end

        it "forwards message to authentication handler" do
          message = client_message
          actor_ref.should_receive(:tell).with(message,anything())
          RequestHandler.should_receive_message(message,'test_request_handler') do
            RequestHandler.find('test_request_handler').tell(message)
          end
        end

        it "calls unhandled if not a client message" do
          RequestHandler.any_instance.should_receive(:unhandled).with('test')
          RequestHandler.should_receive_message('test','test_request_handler') do
            RequestHandler.find('test_request_handler').tell('test')
          end
        end

        it "calls unhandled if player is not set" do
          message = client_message
          message.set_player(nil)
          RequestHandler.any_instance.should_receive(:unhandled).with(message)
          RequestHandler.should_receive_message(message,'test_request_handler') do
            RequestHandler.find('test_request_handler').tell(message)
          end
        end

      end


    end
  end
end
