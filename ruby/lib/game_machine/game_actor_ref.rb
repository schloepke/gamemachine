module GameMachine
  class GameActorRef

    attr_reader :path

    def initialize(path)
      @path = path
    end

    def send_message(message,options={})
      default_options = {:sender => nil, :timeout => 100, :blocking => false}
      options = default_options.merge(options)
      if options[:blocking]
        ask(message,options[:timeout])
      else
        tell(message,options[:sender])
        true
      end
    end

    private

    def actor_selection
      Server.instance.actor_system.actor_selection(path)
    end

    def tell(message,sender)
      actor_selection.tell(message,sender)
    end

    def ask(message,timeout)
      duration = JavaLib::Duration.create(timeout, java.util.concurrent.TimeUnit::MILLISECONDS)
      t = JavaLib::Timeout.new(duration)
      ref = JavaLib::AskableActorSelection.new(actor_selection)
      future = ref.ask(message,t)
      JavaLib::Await.result(future, duration)
    rescue Java::JavaUtilConcurrent::TimeoutException => e
      GameMachine.logger.warn("TimeoutException caught in ask (timeout = #{timeout})")
      false
    end

  end
end
