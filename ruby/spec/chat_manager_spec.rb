require 'spec_helper'

module GameMachine
  module Systems
    
    describe ChatManager do

      let(:chat_channel) { ChatChannel.new.set_name('channel1') }

      let(:chat_message) do
        message = ChatMessage.new
        message.set_chat_channel(chat_channel)
        message.set_message('test').set_type('group')
        player = Player.new.set_authtoken('authorized').set_id('1')
        Entity.new.set_id('1').set_player(player).set_chat_message(message)
      end

      let(:actor_builder) {mock('ActorBuilder', :with_parent => actor_builder, :start => true)}

      let(:chat_manager) do
        props = JavaLib::Props.new(Systems::ChatManager);
        ref = JavaLib::TestActorRef.create(Server.instance.actor_system, props, 'chat_test_manager');
        ref.underlying_actor
      end

      describe "managing chat messages" do
        before(:each) do
        end

        it "creates chat actor for player if it does not exist" do
          ChatManager.any_instance.stub(:forward_chat_request)
          ChatManager.any_instance.should_receive(:create_chat_child)
          ChatManager.should_receive_message(chat_message) do
            ChatManager.find.tell(chat_message)
          end
        end

        it "sends message to player chat actor" do
          Chat.any_instance.should_receive(:on_receive).with(chat_message)
          ChatManager.should_receive_message(chat_message) do
            ChatManager.find.tell(chat_message)
          end
        end

      end

    end

  end
end


