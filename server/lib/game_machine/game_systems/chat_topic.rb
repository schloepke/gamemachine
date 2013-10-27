module GameMachine
  module GameSystems
    class ChatTopic < Actor::Base

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
        entity = MessageLib::Entity.new
        player = MessageLib::Player.new.set_id(@player_id)
        entity.set_id(player.id)
        entity.set_player(player)
        entity.set_chat_message(chat_message)
        entity.set_send_to_player(true)
        PlayerGateway.find.tell(entity)
      end

    end
  end
end

