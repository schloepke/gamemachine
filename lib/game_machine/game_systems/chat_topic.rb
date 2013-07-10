module GameMachine
  module GameSystems
    class ChatTopic < Actor::Base

      def post_init(*args)
        @player_id = args.first
      end

      def on_receive(message)
        if message.is_a?(String)
          receive_chat_message(message)
        elsif message.is_a?(JavaLib::DistributedPubSubMediator::SubscribeAck)
        else
          unhandled(message)
        end
      end

      private

      def receive_chat_message(text)
        GameMachine.logger.debug "Sending chat message #{text} to #{@player_id}"
        message = GameMachine::Helpers::GameMessage.new(@player_id)
        message.chat_message('group',text,@player_id)
        message.send_to_player
      end

    end
  end
end

