module GameMachine
  class MessageQueue < Actor::Base

    def preStart
      if getContext.system.name == 'cluster'
        @mediator = JavaLib::DistributedPubSubExtension.get(get_context.system).mediator
      end
    end

    def on_receive(message)
      unless @mediator
        unhandled(message)
        return
      end
      if message.is_a?(Publish)
        publish(message)
      elsif message.is_a?(Subscribe)
        subscribe(message)
      elsif message.is_a?(Unsubscribe)
        unsubscribe(message)
      elsif message.is_a?(JavaLib::DistributedPubSubMediator::SubscribeAck)
        GameMachine.logger.debug "Subscribed"
      else
        unhandled(message)
      end
    end

    private

    def subscribe(message)
      sub = Java::akka::contrib::pattern::DistributedPubSubMediator::Subscribe.new(message.topic, get_sender)
      @mediator.tell(sub, get_sender)
    end

    def unsubscribe(message)
      sub = Java::akka::contrib::pattern::DistributedPubSubMediator::Unsubscribe.new(message.topic, get_sender)
      @mediator.tell(sub, get_sender)
    end

    def publish(publish)
      if publish.message.has_chat_message
        publish_message = publish.message.chat_message.message
      else
        public_message = publish.message
      end
      message = Java::akka::contrib::pattern::DistributedPubSubMediator::Publish.new(publish.topic, publish_message)
      @mediator.tell(message, get_sender)
    end

  end
end
