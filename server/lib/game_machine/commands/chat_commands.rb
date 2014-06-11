module GameMachine
  module Commands
    class ChatCommands
      include MessageHelper

      def send_private_message(text,player_id)
        message = chat_message('private',text,player_id,player_id)
        entity = entity_with_player(player_id,player_id).
          set_chat_message(message.chat_message)
        GameSystems::ChatManager.find.tell(entity)
      end

      def send_group_message(topic,text,player_id)
        message = chat_message('group',text,topic)
        entity = entity_with_player(player_id,player_id).set_chat_message(message)
        GameSystems::ChatManager.find.tell(entity)
      end

      def join(topic,player_id,invite_id=nil)
        GameSystems::ChatManager.find.tell(join_message(topic,player_id,invite_id))
      end

      def leave(topic,player_id)
        GameSystems::ChatManager.find.tell(leave_message(topic,player_id))
      end

      def invite(topic,inviter,invitee)
        GameSystems::ChatManager.find.tell(invite_message(inviter,invitee,topic))
      end

      def subscribers(topic)
        GameMachine::GameSystems::Chat.subscribers_for_topic(topic)
      end

      def invite_message(inviter,invitee,topic)
        entity_with_player(inviter,inviter).set_chat_invite(
          MessageLib::ChatInvite.new.set_inviter(inviter).
          set_invitee(invitee).set_channel_name(topic)
        )
      end

      def join_message(topic,player_id,invite_id=nil)
       join_msg = MessageLib::JoinChat.new
       chat_channel = chat_channel(topic)
       if invite_id
         chat_channel.set_invite_id(invite_id)
       end
       join_msg.add_chat_channel(chat_channel)
       entity_with_player(player_id,player_id).set_join_chat(join_msg)
      end

      def leave_message(topic,player_id)
       entity_with_player(player_id,player_id).set_leave_chat(
         MessageLib::LeaveChat.new.add_chat_channel(chat_channel(topic))
       )
      end

      def chat_message(type,message_text,topic,sender_id='')
        chat_message = MessageLib::ChatMessage.new.set_chat_channel(
          chat_channel(topic)
        ).set_message(message_text).set_type(type).set_sender_id(sender_id)
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
