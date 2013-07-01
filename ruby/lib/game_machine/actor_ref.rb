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

    def tell(message,sender=nil)
      actor.tell(message,sender_for(sender))
    end

    def ask(message,timeout)
      duration = duration_in_ms(timeout)
      t = JavaLib::Timeout.new(duration)
      if actor.is_a?(JavaLib::ActorSelection)
        sel = JavaLib::AskableActorSelection.new(actor)
        future = sel.ask(message,t)
      else
        future = JavaLib::Patterns::ask(actor,message,t)
      end
      JavaLib::Await.result(future, duration)
    rescue Java::JavaUtilConcurrent::TimeoutException => e
      GameMachine.logger.warn("TimeoutException caught in ask (timeout = #{timeout})")
      false
    end

    private

    def duration_in_ms(ms)
      JavaLib::Duration.create(ms, java.util.concurrent.TimeUnit::MILLISECONDS)
    end

    def actor
      @path_or_actor_ref.is_a?(JavaLib::ActorRef) ? @path_or_actor_ref : actor_selection
    end

    def sender_for(sender)
      sender.is_a?(Actor) ? sender.get_self : sender
    end

    def default_options
      {:sender => nil, :timeout => 100, :blocking => false}
    end

    def actor_selection
      Server.instance.actor_system.actor_selection(@path_or_actor_ref)
    end

  end
end
