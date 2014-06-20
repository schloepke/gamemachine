module GameMachine
  module Actor

    class Ref

      def initialize(path_or_actor_ref,metric_name=nil)
        @path_or_actor_ref = path_or_actor_ref
        @metric_name = metric_name
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

      def actor
        @actor ||= @path_or_actor_ref.is_a?(JavaLib::ActorRef) ? @path_or_actor_ref : actor_selection
      end

      def path
        @path_or_actor_ref.is_a?(String) ? @path_or_actor_ref : nil
      end

      def tell(message,sender=nil)
        message = convert_if_model(message)
        actor.tell(message,sender_for(sender))
        true
      end

      def ask(message,timeout)
        message = convert_if_model(message)
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
        GameMachine.logger.debug("TimeoutException caught in ask (timeout = #{timeout})")
        false
      end

      private

      def convert_if_model(message)
        if message.is_a?(GameMachine::Model)
          message.to_entity
        else
          message
        end
      end

      def duration_in_ms(ms)
        JavaLib::Duration.create(ms, java.util.concurrent.TimeUnit::MILLISECONDS)
      end

      def sender_for(sender)
        @sender_for ||= sender.is_a?(Actor::Base) ? sender.get_self : sender
      end

      def default_options
        {:sender => nil, :timeout => 100, :blocking => false}
      end

      def actor_selection
        Akka.instance.actor_system.actor_selection(@path_or_actor_ref)
      end

    end
  end
end
