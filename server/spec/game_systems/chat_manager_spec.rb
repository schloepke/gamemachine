require 'spec_helper'

module GameMachine
  module GameSystems
    
    describe ChatManager do

      let(:player_id) {'player1'}
      let(:topic) {'test_topic'}
      let(:chat_text) {'test text'}
      let(:client_id) {'player1_id'}
      let(:gateway) {'blah'}

      let(:game_message) do
        Helpers::GameMessage.new(player_id)
      end

      let(:join_request) do
        game_message.join_chat(topic)
        game_message.to_entity
      end

      let(:chat_message) do
        game_message.chat_message('group',chat_text,topic)
        game_message.to_entity
      end

      let(:actor_builder) {mock('Actor::Builder', :with_parent => actor_builder, :start => true)}

      let(:disconnected) do
        MessageLib::Disconnected.new.set_client_id(client_id).set_player_id(player_id)
      end

      let(:chat_invite) do
        chat = Commands::ChatCommands.new
        chat.invite_message('inviter','invitee','test')
      end

      subject do
        ref = Actor::Builder.new(GameSystems::ChatManager).with_name('chat_manager').test_ref
        ref.underlying_actor
      end

      describe "chat invites" do
        it "chat manager calls send_invite when it receives a chat invite" do
          expect(subject).to receive(:send_invite).with(chat_invite.chat_invite)
          subject.on_receive(chat_invite)
        end

      end

      describe "multiple players" do

      end

      describe "managing chat messages" do

        before(:each) do
          PlayerRegistry.register_player(
            game_message.client_message.player.id,
            game_message.client_connection(client_id,gateway)
          )
        end

        it "creates chat actor for player if it does not exist" do
          expect(subject).to receive(:forward_chat_request)
          subject.on_receive(chat_message)
        end

        it "destroys chat actor on client disconnect" do
          ChatManager.should_receive_message(join_request) do
            ChatManager.find.tell(join_request)
          end
          ChatManager.any_instance.should_receive(:destroy_child).with(disconnected.player_id)
          ChatManager.should_receive_message(disconnected) do
            ChatManager.find.tell(disconnected)
          end
        end

      end

    end

  end
end


