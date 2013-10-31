module GameMachine
  module GameSystems
    class ChatTopic < Actor::Base
      include Commands

      def post_init(*args)
        @player_id = args.first
      end

      def on_receive(message)
        if message.is_a?(MessageLib::Entity) && message.has_chat_message
          receive_chat_message(message.chat_message)
        elsif message.is_a?(JavaLib::DistributedPubSubMediator::SubscribeAck)
        else
          unhandled(message)
        end
      end

      private

      def receive_chat_message(chat_message)
        GameMachine.logger.info "Sending chat message #{chat_message.message} to #{@player_id}"
        commands.player.send_message(chat_message,@player_id)
      end

    end
  end
end

