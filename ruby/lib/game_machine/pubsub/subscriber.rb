module GameMachine
  module Pubsub
    class Subscriber < Actor

      def preStart
        if getContext.system.name == 'cluster'
          mediator = JavaLib::DistributedPubSubExtension.get(get_context.system).mediator
          sub = JavaLib::DistributedPubSubMediator::Subscribe.new("content", get_self)
          mediator.tell(sub, get_self)
        end
      end

      def on_receive(message)
        if message.is_a?(JavaLib::DistributedPubSubMediator::SubscribeAck)
          GameMachine.logger.info "Subscribed"
        end
      end
    end
  end
end
