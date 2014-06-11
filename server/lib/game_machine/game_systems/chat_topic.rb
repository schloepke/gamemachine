module GameMachine
  module GameSystems
    class ChatTopic < Actor::Base
      include Commands

      attr_reader :chat_id, :registered_as
      def post_init(*args)
        @chat_id = args.first
        @registered_as = args.last
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
        GameMachine.logger.info "Sending chat message #{chat_message.message} to #{chat_id}"
        if registered_as == 'player'
          commands.player.send_message(chat_message,chat_id)
        else
          message = MessageLib::Entity.new.set_id(chat_id).set_chat_message(chat_message)
          Actor::Base.find(registered_as).tell(message,get_self)
        end
      end

    end
  end
end

