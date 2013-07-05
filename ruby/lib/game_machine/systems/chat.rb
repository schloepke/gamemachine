module GameMachine
  module Systems
    class Chat < Actor

      def post_init(*args)
        @players = {}
      end

      def on_receive(entity)
        if entity.has_join_chat
          join_channels(entity.join_chat.get_chat_channel_list)
        end

        if entity.has_leave_chat
          leave_channels(entity.leave_chat.get_chat_channel_list)
        end

        if entity.has_chat_message
          send_message(entity.chat_message)
        end

        if entity.has_chat_register
          @players[entity.player.id] = entity.client_connection
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

      def send_private_message(chat_message)
        if @players.has_key?(chat_message.chat_channel.name)
          client_connection = @players.fetch(chat_message.chat_channel.name)
          client_message = ClientMessage.new
          client_message.add_entity(Entity.new.set_id('0').set_chat_message(chat_message))
          client_message.set_client_connection(client_connection)
          client_message.send_to_client
        end
      end

      def send_group_message(chat_message)
        chat_channels.each do |channel|
          publish = Publish.new
          publish.set_topic(channel.name).set_message(chat_message.message)
          message_queue.tell(publish,self)
        end
      end

      def send_message(chat_message)
        if chat_message.type == 'group'
          send_group_message(chat_message)
        elsif chat_message.type == 'private'
          send_private_message(chat_message)
        end
      end

    end
  end
end
