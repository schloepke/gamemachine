module GameMachine
  class MessageQueue < Actor


    def post_init(*args)
    end

    def preStart
      if getContext.system.name == 'cluster'
        @mediator = JavaLib::DistributedPubSubExtension.get(get_context.system).mediator
      end
    end

    def subscribe(message)
      return unless @mediator
      sub = JavaLib::DistributedPubSubMediator::Subscribe.new(message.topic, get_sender)
      @mediator.tell(sub, get_sender)
    end

    def publish(message)
      return unless @mediator
      message = JavaLib::DistributedPubSubMediator::Publish.new(message.topic, message.message)
      @mediator.tell(message, get_sender)
    end

    def on_receive(message)
      if message.is_a?(Publish)
        publish(message)
      elsif message.is_a?(Subscribe)
        subscribe(message)
      elsif message.is_a?(JavaLib::DistributedPubSubMediator::SubscribeAck)
        GameMachine.logger.info "Subscribed"
      else
        unhandled(message)
      end
    end
  end
end
