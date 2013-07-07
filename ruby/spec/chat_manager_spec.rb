require 'spec_helper'

module GameMachine
  module Systems
    
    describe ChatManager do

      let(:player_id) {'player1'}
      let(:topic) {'test_topic'}
      let(:chat_text) {'test text'}
      let(:client_id) {'player1_id'}
      let(:gateway) {'blah'}

      let(:game_message) do
        Helpers::GameMessage.new(player_id,client_id,gateway)
      end

      let(:join_request) do
        game_message.join_chat(topic)
        game_message.to_entity
      end

      let(:chat_message) do
        game_message.chat_message('group',chat_text,topic)
        game_message.to_entity
      end

      let(:actor_builder) {mock('ActorBuilder', :with_parent => actor_builder, :start => true)}

      let(:chat_manager) do
        props = JavaLib::Props.new(Systems::ChatManager);
        ref = JavaLib::TestActorRef.create(Server.instance.actor_system, props, 'chat_test_manager');
        ref.underlying_actor
      end

      let(:client_disconnect) {ClientDisconnect.new.set_client_id(client_id)}

      describe "managing chat messages" do
        before(:each) do
        end

        it "creates chat actor for player if it does not exist" do
          Chat.any_instance.should_receive(:on_receive).with(chat_message)
          ChatManager.should_receive_message(chat_message) do
            ChatManager.find.tell(chat_message)
          end
        end

        it "destroys chat actor on client disconnect" do
          ChatManager.should_receive_message(join_request) do
            ChatManager.find.tell(join_request)
          end
          ChatManager.any_instance.should_receive(:destroy_child).with(client_disconnect)
          ChatManager.should_receive_message(client_disconnect) do
            ChatManager.find.tell(client_disconnect)
          end
        end

      end

    end

  end
end


