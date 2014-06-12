
# Shows how to use the messaging/chat system programmatically from the server.
# Also shows how to use the schedular to send ourselves a recurring message that
# we can use to trigger recurring actions.

module Example
  class Chatbot < GameMachine::Actor::Base
    include GameMachine
    include GameMachine::Commands

    attr_reader :topic, :chatbot_id
    def post_init(*args)
      @topic = args.first
      @chatbot_id = 'overlord'

      # On the server side we use register, which takes our chat id
      # and the name of this actor.
      commands.chat.register(chatbot_id,'Example::Chatbot')

      # Then we send a regular join request
      commands.chat.join('global',chatbot_id)

      # Send ourselves a 'doit' message every 5 seconds
      schedule_message('doit',5,:seconds)
    end

    def message_queue
      MessageQueue.find
    end

    def on_receive(message)

      # This is our scheduled message
      if message.is_a?(String)
        if message == 'doit'
          commands.chat.send_group_message('global','Would you like to play a game?',chatbot_id)
        end

      # Status update we get from chat system, we don't care..
      elsif message.has_chat_channels

      # Must be a chat message
      elsif message.has_chat_message

        # ignore our own messages
        sender_id = message.chat_message.sender_id
        unless sender_id == chatbot_id
          text = message.chat_message.message
          GameMachine.logger.info "Chatbot received text #{text}"

          # Respond with a private message
          if text.match(/shut up/)
            reply = 'Same to you!'
            commands.chat.send_private_message(reply,sender_id)
          end

        end
      end
    end
  end
end
