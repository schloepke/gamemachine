module GameMachine
  module Actor

    class Factory
      include JavaLib::UntypedActorFactory

      def initialize(klass,args)
        @klass = klass
        @args = args
      end

      def create
        if @klass.respond_to?(:reload_on_change?)
          if @klass.reload_on_change?
            GameMachine.logger.info "Checking if #{@klass.name} should be reloaded"
            GameMachine::Actor::Reloadable.reload_if_changed(@klass.name)
            @klass = @klass.name.constantize
          end
        end
        actor = @klass.new
        if actor.respond_to?(:initialize_states)
          actor.initialize_states
        end
        if actor.respond_to?(:post_init)
          actor.post_init(*@args)
        end
        actor
      end

      def self.create
      end

    end
  end
end
