module GameMachine
  class ActorFactory
    include UntypedActorFactory

    def initialize(klass,args)
      @klass = klass
      @args = args
    end

    def create
      actor = @klass.new
      if @args.size >= 1 && actor.respond_to?(:post_init)
        actor.post_init(*@args)
      end
      actor
    end

    def self.create
    end
  end
end
