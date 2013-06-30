module GameMachine
  class ActorRef


    def initialize(path_or_actor_ref)
      @path_or_actor_ref = path_or_actor_ref
    end

    def send_message(message,options={})
      options = default_options.merge(options)
      sender = sender_for(options[:sender])

      if options[:blocking]
        ask(message,options[:timeout])
      else
        tell(message,sender)
        true
      end
    end

    def path
      @path_or_actor_ref.is_a?(String) ? @path_or_actor_ref : nil
    end

    def actor
      @path_or_actor_ref.is_a?(JavaLib::ActorRef) ? @path_or_actor_ref : actor_selection
    end


    private

    def sender_for(sender)
      sender.is_a?(Actor) ? sender.get_self : sender
    end

    def default_options
      {:sender => nil, :timeout => 100, :blocking => false}
    end

    def actor_selection
      Server.instance.actor_system.actor_selection(@path_or_actor_ref)
    end

    def tell(message,sender)
      actor.tell(message,sender)
    end

    def ask(message,timeout)
      duration = JavaLib::Duration.create(timeout, java.util.concurrent.TimeUnit::MILLISECONDS)
      t = JavaLib::Timeout.new(duration)
      ref = JavaLib::AskableActorSelection.new(actor)
      future = ref.ask(message,t)
      JavaLib::Await.result(future, duration)
    rescue Java::JavaUtilConcurrent::TimeoutException => e
      GameMachine.logger.warn("TimeoutException caught in ask (timeout = #{timeout})")
      false
    end

  end
end
