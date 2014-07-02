require 'spec_helper'

module GameMachine
  module GameSystems
    describe Chat do

      let(:player_id) {'player1'}
      let(:topic) {'test_topic'}
      let(:chat_text) {'test text'}
      let(:client_id) {'player1_id'}
      let(:gateway) {'blah'}

      let(:commands) do
        Commands::Base.new
      end

      let(:game_message) do
        Helpers::GameMessage.new(player_id)
      end

      let(:leave_request) do
        game_message.leave_chat(topic)
        game_message.to_entity
      end

      let(:join_request_with_flags) do
        flags = 'subscribers|blah'
        game_message.join_chat(topic,flags)
        game_message.to_entity
      end

      let(:join_request) do
        game_message.join_chat(topic)
        game_message.to_entity
      end

      let(:public_chat_request) do
        game_message.chat_message('group',chat_text,topic)
        game_message.to_entity
      end

      let(:private_chat_request) do
        game_message.chat_message('private',chat_text,topic)
        game_message.to_entity
      end

      let(:all_requests) do
        game_message.join_chat(topic)
        game_message.chat_message('group',chat_text,topic)
        game_message.leave_chat(topic)
        game_message.to_entity
      end

      let(:actor_ref) {double('Actor::Ref', :tell => true)}

      subject do
        ref = Actor::Builder.new(GameSystems::Chat,player_id).with_name('chat_test').test_ref
        actor = ref.underlying_actor
        actor
      end

      before(:each) do
        Chat.any_instance.stub(:load_state)
        Actor::Builder.new(GameSystems::Chat,player_id).start
        MessageQueue.stub(:find).and_return(actor_ref)
        commands.datastore.delete("chat_topic_#{topic}")
      end

      describe "private channels" do

        it "joining private channel without invite should fail" do
          channel_name = "priv/otherplayer/test"
          join_message = commands.chat.join_message(channel_name,player_id)
          expect(subject).to_not receive(:join_channel)
          subject.on_receive(join_message)
        end

        it "joining my own channel should work" do
          channel_name = "priv_#{player_id}_test"
          join_message = commands.chat.join_message(channel_name,player_id)
          expect(subject).to receive(:join_channel)
          subject.on_receive(join_message)
        end

        it "joining private channel with invite should work" do
          channel_name = "priv/otherplayer/test"
          join_message = commands.chat.join_message(channel_name,player_id,'myinvite')
          expect(subject).to receive(:join_channel)
          expect(subject).to receive(:invite_exists?).with(channel_name,'myinvite').and_return(true)
          subject.on_receive(join_message)

        end
      end

      describe "#subscribers_for_topic" do
        it "subscriber id count is zero when no subscribers" do
          subscribers = Chat.subscribers_for_topic(topic)
          expect(subscribers.get_subscriber_id_count).to eql(0)
        end
      end

      describe "subscribers list updates" do
        it "joining a channel adds player to subscriber list" do
          expect(subject).to receive(:add_subscriber)
          subject.on_receive(join_request)
          subject.on_receive(leave_request)
        end

        it "leaving a channel removes player as subscriber" do
          subject.on_receive(join_request)
          subject.on_receive(leave_request)
          subscribers = Chat.subscribers_for_topic(topic)
          expect(subscribers.get_subscriber_id_count).to eql(0)
        end
      end

      describe "channel flags" do
        it "joining a channel with flags should persist the flag" do
          subject.on_receive(join_request_with_flags)
          topic_flags = subject.get_flags.fetch(topic,[])
          topic_flags.include?('subscribers').should be_true
        end

        it "chat status should add subscribers to channel if flag is set" do
          subject.on_receive(join_request_with_flags)
          expect_any_instance_of(MessageLib::ChatChannel).to receive(:set_subscribers)
          entity = MessageLib::Entity.new.set_chat_status(MessageLib::ChatStatus.new)
          subject.on_receive(entity)
        end
      end

      describe "subscriptions" do
        it "joining a channel should persist subscriptions" do
          subject.on_receive(join_request_with_flags)
          subscriptions = subject.get_subscriptions
          subscriptions.include?(topic).should be_true
        end
      end

      describe "joining and leaving channels" do
        it "processes the entity as a leave channel request" do
          subject.should_receive(:leave_channels)
          subject.on_receive(leave_request)
        end

        it "sends an unsubscribe message to message queue" do
          subject.on_receive(join_request)
          actor_ref.should_receive(:tell).with(kind_of(MessageLib::Unsubscribe),anything())
          subject.on_receive(leave_request)
        end

        it "processes the entity as a join channel request" do
          subject.should_receive(:join_channels)
          subject.on_receive(join_request)
        end

        it "sends a subscribe message to message queue" do
          actor_ref.should_receive(:tell).exactly(1).times
          subject.on_receive(join_request)
        end

        it "processes multiple requests in a single message" do
          subject.should_receive(:join_channels).once
          subject.should_receive(:leave_channels).once
          subject.should_receive(:send_message).once
          subject.on_receive(all_requests)
        end

        it "send a private chat message" do
          subject.should_receive(:send_private_message).once
          subject.on_receive(private_chat_request)
        end

        it "send a group chat message" do
          subject.should_receive(:send_group_message).once
          subject.on_receive(public_chat_request)
        end

      end

    end

  end
end

