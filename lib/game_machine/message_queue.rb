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
      if message.topic
        sub = Java::akka::contrib::pattern::DistributedPubSubMediator::Subscribe.new(message.topic, get_sender)
      else
        sub = Java::akka::contrib::pattern::DistributedPubSubMediator::Put.new(get_sender)
      end
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
        publish_message = publish.message
      end
      if publish.topic
        message = Java::akka::contrib::pattern::DistributedPubSubMediator::Publish.new(publish.topic, publish_message)
      elsif publish.path
        message = Java::akka::contrib::pattern::DistributedPubSubMediator::SendToAll.new(publish.path, publish_message,true)
      else
        GameMachine.logger.error("Publish missing topic or path")
        return
      end
      @mediator.tell(message, get_sender)
    end

  end
end
