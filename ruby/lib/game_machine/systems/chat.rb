module GameMachine
  module Systems
    class Chat < Actor

      def post_init(*args)
        @player_id = args.first
        @client_connection = args.last
        @topic_handlers = {}
        @subscriptions = []
      end

      def on_receive(entity)
        if entity.has_join_chat
          join_channels(entity.join_chat.get_chat_channel_list)
        end

        if entity.has_chat_message
          send_message(entity.chat_message)
        end

        if entity.has_leave_chat
          leave_channels(entity.leave_chat.get_chat_channel_list)
        end
        send_client_update(entity)
      end

      private

      def send_client_update(entity)
        m = Helpers::GameMessage.new(@player_id)
        m.chat_channels(@subscriptions)
        m.client_connection(entity.client_connection)
        m.client_message.send_to_client
      end

      def message_queue
        MessageQueue.find
      end

      def topic_handler_for(name)
        @topic_handlers.fetch(name)
      end

      def create_topic_handler(topic)
        name = "topic#{@player_id}#{topic}"
        builder = ActorBuilder.new(Systems::ChatTopic,@player_id,@client_connection)
        ref = builder.with_parent(context).with_name(name).start
        actor_ref = ActorRef.new(ref,Systems::ChatTopic.name)
        @topic_handlers[topic] = actor_ref
      end

      def join_channels(chat_channels)
        chat_channels.each do |channel|
          next if @topic_handlers[channel.name]
          create_topic_handler(channel.name)
          message = Subscribe.new.set_topic(channel.name)
          message_queue.tell(message,topic_handler_for(channel.name).actor)
          @subscriptions << channel.name
        end
      end

      def leave_channels(chat_channels)
        chat_channels.each do |channel|
          message = Unsubscribe.new.set_topic(channel.name)
          message_queue.tell(message,topic_handler_for(channel.name).actor)
          @subscriptions.delete_if {|sub| sub == channel.name}
        end
      end

      def send_private_message(chat_message)
        if @players.has_key?(chat_message.chat_channel.name)
          client_connection = @players.fetch(chat_message.chat_channel.name)
          client_message = ClientMessage.new
          entity = Entity.new.set_id('0').set_chat_message(chat_message)
          client_message.add_entity(entity)
          client_message.set_client_connection(client_connection)
          client_message.send_to_client
        end
      end

      def send_group_message(chat_message)
        topic = chat_message.chat_channel.name
        entity = Entity.new.set_id('0').set_chat_message(chat_message)
        publish = Publish.new
        publish.set_topic(topic).set_message(entity)
        message_queue.tell(publish,topic_handler_for(topic).actor)
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
