module GameMachine
  class MessageQueue < Actor

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
      sub = JavaLib::DistributedPubSubMediator::Subscribe.new(message.topic, get_sender)
      @mediator.tell(sub, get_sender)
    end

    def unsubscribe(message)
      sub = JavaLib::DistributedPubSubMediator::Unsubscribe.new(message.topic, get_sender)
      @mediator.tell(sub, get_sender)
    end

    def publish(message)
      message = JavaLib::DistributedPubSubMediator::Publish.new(message.topic, message.message)
      @mediator.tell(message, get_sender)
    end

  end
end
