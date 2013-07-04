module GameMachine
  module Systems
    class Chat < Actor

      register_components %w(ChatMessage JoinChatChannels LeaveChatChannels)

      def on_receive(message)
        if message.has_join_chat_channels
          join_channels(message.get_chat_channel_list)
        end
        if message.has_leave_chat_channels
          leave_channels(message.get_chat_channel_list)
        end
        if message.has_chat_message
          send_message(message.chat_message)
        end
      end

      private

      def message_queue
        MessageQueue.find
      end

      def join_channels(chat_channels)
        chat_channels.each do |channel|
          message_queue.tell(Subscribe.new.set_topic(channel.name),self)
        end
      end

      def leave_channels(chat_channels)
        chat_channels.each do |channel|
          message_queue.tell(Unsubscribe.new.set_topic(channel.name),self)
        end
      end

      def send_message(chat_message)
        chat_channels.each do |channel|
          publish = Publish.new
          publish.set_topic(channel.name).set_message(chat_message.message)
          message_queue.tell(publish,self)
        end
      end

    end
  end
end
