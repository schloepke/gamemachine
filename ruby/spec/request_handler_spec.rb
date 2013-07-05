require 'spec_helper'

module GameMachine
  module Systems
    describe RequestHandler do

      let(:player) {Player.new.set_id('1')}
      let(:client_connection) {ClientConnection.new}

      let(:entity) do
       Entity.new.set_id('1')
      end

      let(:client_message) do
        ClientMessage.new.
          set_client_connection(client_connection).
          set_player(player).
          add_entity(entity)
      end

      let(:actor_ref) {mock('ActorRef', :tell => true)}

      describe "#on_receive" do

        before(:each) do
          AuthenticationHandler.stub(:find_distributed_local).and_return(actor_ref)
          ActorBuilder.new(RequestHandler).with_name('test_request_handler').start
        end

        it "sets client connection on entities" do
          message = client_message
          entity = message.get_entity_list.first
          entity.should_receive(:set_client_connection).with(message.client_connection)
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
