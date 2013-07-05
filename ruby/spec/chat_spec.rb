require 'spec_helper'

module GameMachine
  module Systems
    
    describe Chat do

      let(:chat_channel) { ChatChannel.new.set_name('channel1') }

      let(:player) do
        player = Player.new.set_authtoken('authorized').set_id('1')
      end

      let(:chat_message) do
        message = ChatMessage.new
        message.set_chat_channel(chat_channel)
        message.set_message('test')
        message.set_type('group')
      end

      let(:private_chat_message) do
        message = ChatMessage.new
        message.set_chat_channel(chat_channel)
        message.set_message('test')
        message.set_type('private')
      end

      let(:public_chat_request) do
        Entity.new.set_id('1').
          set_player(player).
          set_chat_message(chat_message)
      end

      let(:private_chat_request) do
        Entity.new.set_id('1').
          set_player(player).
          set_chat_message(private_chat_message)
      end

      let(:join_chat) do
        JoinChat.new.add_chat_channel(chat_channel)
      end

      let(:leave_chat) do
        LeaveChat.new.add_chat_channel(chat_channel)
      end

      let(:join_request) do
        Entity.new.set_id('1').set_player(player).set_join_chat(join_chat)
      end

      let(:leave_request) do
        Entity.new.set_id('1').set_player(player).set_leave_chat(leave_chat)
      end

      let(:leave_request_multiple) do
        leave_request.set_leave_chat(leave_chat.add_chat_channel(chat_channel))
      end

      let(:join_request_multiple) do
        leave_request.set_leave_chat(leave_chat.add_chat_channel(chat_channel))
      end

      let(:all_requests) do
        entity = Entity.new.set_id('1').set_player(player)
        entity.set_join_chat(join_chat)
        entity.set_leave_chat(leave_chat)
        entity.set_chat_message(chat_message)
      end

      let(:chat) do
        props = JavaLib::Props.new(Systems::Chat);
        ref = JavaLib::TestActorRef.create(Server.instance.actor_system, props, 'chat_test');
        ref.underlying_actor
      end

      let(:actor_ref) {mock('ActorRef', :tell => true)}

      describe "joining and leaving channels" do
        before(:each) do
          ActorBuilder.new(Systems::Chat).start
          MessageQueue.stub(:find).and_return(actor_ref)
        end

        it "processes the entity as a leave channel request" do
          Chat.any_instance.should_receive(:leave_channels)
          Chat.should_receive_message(leave_request) do
            Chat.find.tell(leave_request)
          end
        end

        it "sends an unsubscribe message to message queue" do
          actor_ref.should_receive(:tell).exactly(1).times
          Chat.should_receive_message(leave_request) do
            Chat.find.tell(leave_request)
          end
        end

        it "sends multiple messages to message queue" do
          actor_ref.should_receive(:tell).exactly(2).times
          Chat.should_receive_message(leave_request_multiple) do
            Chat.find.tell(leave_request_multiple)
          end
        end

        it "processes the entity as a join channel request" do
          Chat.any_instance.should_receive(:join_channels)
          Chat.should_receive_message(join_request) do
            Chat.find.tell(join_request)
          end
        end

        it "sends a subscribe message to message queue" do
          actor_ref.should_receive(:tell).exactly(1).times
          Chat.should_receive_message(join_request) do
            Chat.find.tell(join_request)
          end
        end

        it "processes multiple requests in a single message" do
          Chat.any_instance.should_receive(:join_channels).once
          Chat.any_instance.should_receive(:leave_channels).once
          Chat.any_instance.should_receive(:send_message).once
          Chat.should_receive_message(all_requests) do
            Chat.find.tell(all_requests)
          end
        end

        it "send a private chat message" do
          Chat.any_instance.should_receive(:send_private_message).once
          Chat.should_receive_message(private_chat_request) do
            Chat.find.tell(private_chat_request)
          end
        end

        it "send a group chat message" do
          Chat.any_instance.should_receive(:send_group_message).once
          Chat.should_receive_message(public_chat_request) do
            Chat.find.tell(public_chat_request)
          end
        end

      end

    end

  end
end

