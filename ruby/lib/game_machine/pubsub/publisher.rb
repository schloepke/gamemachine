module GameMachine
  module Pubsub
    class Publisher < GameActor

      def preStart
       @mediator = JavaLib::DistributedPubSubExtension.get(get_context.system).mediator
       message = JavaLib::DistributedPubSubMediator::Publish.new("content", 'test')
	     @mediator.tell(message, get_self)
      end

      def on_receive(message)
        puts message.inspect
      end
    end
  end
end

