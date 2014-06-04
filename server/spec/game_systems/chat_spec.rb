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
        ref = Actor::Builder.new(GameSystems::Chat).with_name('chat_test').test_ref
        ref.underlying_actor.post_init(player_id)
        ref.underlying_actor
      end

      before(:each) do
        Actor::Builder.new(GameSystems::Chat,player_id).start
        MessageQueue.stub(:find).and_return(actor_ref)
        PlayerRegistry.register_player(
          game_message.client_message.player.id,
          game_message.client_connection(client_id,gateway)
        )
        commands.datastore.delete("chat_topic_#{topic}")
      end

      describe "#subscribers_for_topic" do
        it "subscriber id count is zero when no subscribers" do
          subscribers = Chat.subscribers_for_topic(topic)
          expect(subscribers.get_subscriber_id_count).to eql(0)
        end
      end

      describe "subscribers list updates" do
        it "joining a channel adds player to subscriber list" do
          subject.on_receive(join_request)
          subscribers = Chat.subscribers_for_topic(topic)
          expect(subscribers.get_subscriber_id_count).to eql(1)
          expect(subscribers.get_subscriber_id_list.first).to eql(player_id)
        end

        it "leaving a channel removes player as subscriber" do
          subject.on_receive(join_request)
          subject.on_receive(leave_request)
          subscribers = Chat.subscribers_for_topic(topic)
          expect(subscribers.get_subscriber_id_count).to eql(0)
        end
      end

      describe "joining and leaving channels" do
        it "processes the entity as a leave channel request" do
          Chat.any_instance.should_receive(:leave_channels)
          Chat.should_receive_message(leave_request) do
            Chat.find.tell(leave_request)
          end
        end

        it "sends an unsubscribe message to message queue" do
          Chat.should_receive_message(join_request) do
            Chat.find.tell(join_request)
          end
          Chat.should_receive_message(leave_request) do
            actor_ref.should_receive(:tell).with(kind_of(MessageLib::Unsubscribe),anything())
            Chat.find.tell(leave_request)
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

