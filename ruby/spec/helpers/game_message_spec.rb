require 'spec_helper'

module GameMachine
  module Helpers
    
    describe GameMessage do

      let(:player_id) {'test_player'}
      
      let(:entity) do
          subject.client_message.get_entity_list.first
      end

      subject do
        GameMessage.new(player_id)
      end

      describe "creating a new message" do
        before(:each) do
        end

        it "new message has default entity" do
          expect(subject.current_entity.id).to eq('1')
        end

        it "new message has player" do
          expect(subject.client_message.player.id).to eq(player_id)
        end

        it "new entities have incrementing id's" do
          subject.set_entity(:test)
          subject.use_entity(:test)
          expect(subject.current_entity.id).to eq('2')
        end
      end

      describe "#chat_message" do
        it "Adds a chat message with the correct values" do
          subject.chat_message('group','test','region')
          chat_message = entity.chat_message
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
          channel = entity.join_chat.get_chat_channel_list.first
          expect(channel.name).to eq('blah')
        end
      end

      describe "#leave_chat" do
        it "Adds leave chat message with correct values" do
          subject.leave_chat('blah')
          channel = entity.leave_chat.get_chat_channel_list.first
          expect(channel.name).to eq('blah')
        end
      end

    end

  end
end


