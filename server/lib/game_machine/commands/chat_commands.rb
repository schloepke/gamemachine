module GameMachine
  module Commands
    class ChatCommands
      include MessageHelper

      def send_private_message(player_id,text)
        message = chat_message('private',text,player_id)
        GameSystems::Chat.find.tell(message)
      end

      def send_group_message(topic,text)
        message = chat_message('group',text,topic)
        GameSystems::Chat.find.tell(message)
      end

      def join(topic)
        GameSystems::Chat.find.tell(join_message(topic))
      end

      def leave(topic)
        GameSystems::Chat.find.tell(leave_message(topic))
      end

      def subscribers(topic)
        GameMachine::GameSystems::Chat.subscribers_for_topic(topic)
      end

      private

      def join_message(topic)
       entity(entity_id).message.set_join_chat(
         MessageLib::JoinChat.new.add_chat_channel(chat_channel(topic))
       )
      end

      def leave_message(topic)
       entity(entity_id).message.set_leave_chat(
         MessageLib::LeaveChat.new.add_chat_channel(chat_channel(topic))
       )
      end

      def chat_message(type,message_text,topic)
        chat_message = MessageLib::ChatMessage.new.set_chat_channel(
          chat_channel(topic)
        ).set_message(message_text).set_type(type)
        entity(entity_id).set_chat_message(chat_message)
      end

      def entity_id
        '0'
      end

      def chat_channel(topic)
        MessageLib::ChatChannel.new.set_name(topic)
      end

    end
  end
end
