require 'spec_helper'

module GameMachine
  module Helpers
    
    describe GameMessage do

      let(:player_id) {'test_player'}
      let(:client_id) {'client_id1'}
      let(:gateway) {'gateway1'}
      
      subject do
        GameMessage.new(player_id)
      end


      describe "creating a new message" do

        it "new message has no entity" do
          expect(subject.client_message.get_entity_list).to be_nil
        end

        it "new message has player" do
          expect(subject.client_message.player.id).to eq(player_id)
        end
      end

      describe "#entity(id)" do
        it "creates an entity and sets it to the current entity" do
          subject.entity('blah')
          expect(subject.current_entity.id).to eq('blah')
        end

        it "sets the current entity to the id specified" do
          subject.entity('one')
          subject.entity('default')
          expect(subject.current_entity.id).to eq('default')
        end
      end

      describe "#current_entity" do
        it "returns the current entity" do
          expect(subject.current_entity.id).to eq('default')
        end
      end

      describe "#error_message" do
        it "sets error message on client message" do
          subject.error_message('code','message')
          expect(subject.client_message.error_message.code).to eq('code')
          expect(subject.client_message.error_message.message).to eq('message')
        end
      end

      describe "#chat_message" do
        it "Adds a chat message with the correct values" do
          subject.chat_message('group','test','region')
          chat_message = subject.current_entity.chat_message
          expect(chat_message).not_to be_nil
          expect(chat_message.message).to eq('test')
          expect(chat_message.type).to eq('group')
          channel = chat_message.chat_channel
          expect(channel.name).to eq('region')
        end
      end

      describe "#join_chat" do
        it "Adds join chat message with correct values" do
          subject.join_chat('blah')
          channel = subject.current_entity.join_chat.get_chat_channel_list.first
          expect(channel.name).to eq('blah')
        end
      end

      describe "#leave_chat" do
        it "Adds leave chat message with correct values" do
          subject.leave_chat('blah')
          channel = subject.current_entity.leave_chat.get_chat_channel_list.first
          expect(channel.name).to eq('blah')
        end
      end

      describe "#client_disconnect" do
        it "creates client disconnect and sets it on client message" do
          subject.client_disconnect(client_id,gateway)
          expect(subject.client_message.client_disconnect).to be_kind_of(ClientDisconnect)
        end

        it "client disconnect contains client connection" do
          subject.client_disconnect(client_id,gateway)
          client_connection = subject.client_message.client_disconnect.client_connection
          expect(client_connection.id).to eq(client_id)
          expect(client_connection.gateway).to eq(gateway)
        end
      end

      describe "#player_logout" do
        it "sets player logout onn client message" do
          subject.player_logout
          expect(subject.client_message.player_logout).to be_kind_of(PlayerLogout)
        end

        it "player logout contains correct player id" do
          subject.player_logout
          expect(subject.client_message.player_logout.player_id).
            to eq(subject.player_id)
        end
      end

      describe "#client_connection" do
        it "creates and returns client connection with correct values" do
          client_connection = subject.client_connection(client_id,gateway)
          expect(client_connection.id).to eq(client_id)
          expect(client_connection.gateway).to eq(gateway)
        end
      end

      describe "#echo_test" do
        it "sets echo test on current entity" do
          subject.echo_test('blah')
          expect(subject.current_entity.echo_test.message).to eq('blah')
        end
      end
    end

  end
end


