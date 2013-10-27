module GameMachine
  module Commands
    class ChatCommands
      include MessageHelper

      def send_private_message(player_id,text)
        send_message('private',text,player_id)
      end

      def send_group_message(topic,text)
        send_message('group',text,topic)
      end

      def join(topic)
        join_channel(topic)
      end

      def leave(topic)
        leave_channel(topic)
      end

      private

      def join_channel(topic)
       message = entity('0')
       message.set_join_chat(
         JoinChat.new.add_chat_channel(chat_channel(topic))
       )
      end

      def leave_channel(topic)
       message = entity('0')
       message.set_leave_chat(
         JoinChat.new.add_chat_channel(chat_channel(topic))
       )
      end

      def send_message(type,message_text,topic)
        message = ChatMessage.new
        message.set_chat_channel(
          chat_channel(topic)
        )
        message.set_message(message_text)
        message.set_type(type)
        entity('0').set_chat_message(message)
      end

      def chat_channel(topic)
        ChatChannel.new.set_name(topic)
      end

    end
  end
end
