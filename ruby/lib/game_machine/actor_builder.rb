module GameMachine
  class ActorBuilder

    attr_reader :name

    def initialize(*args)
      @klass = args.shift
      @name = @klass.name
      @hashring = nil
      @props = Props.new(ActorFactory.new(@klass,args))
    end

    def with_name(name)
      @name = name
      self
    end

    def with_router(router_class,num_routers)
      @props = @props.with_router(
        router_class.new(num_routers)
      )
      self
    end

    def distributed(count)
      create_hashring(count)
      self
    end

    def start
      if @hashring
        @hashring.buckets.each do |bucket_name|
          actor_system.actor_of(@props, bucket_name)
        end
      else
        actor_system.actor_of(@props, @name)
      end
    end


    private

    def actor_system
      GameMachineLoader.get_actor_system
    end

    def create_hashring(bucket_count)
        @hashring = Hashring.new(
          Settings.servers.keys,
          @name
        )
        @klass.hashring = @hashring
    end

  end
end
