require 'jruby/core_ext'
module GameMachine
  module Actor

    class Builder

      attr_reader :name

      # Creates an actor builder instance.  First argument is the actor class
      # remaining arguments are optional and will be passed to post_init
      # @param args 
      # @return self
      def initialize(*args)
        @klass = args.shift
        @name = @klass.name
        @create_hashring = false
        @hashring_size = 0
        factory = Actor::Factory.new(@klass,args)
        @props = JavaLib::Props.create(
          JavaLib::ActorFactory.java_class,factory,@klass.name)
        @actor_system = Akka.instance.actor_system
      end

      # Special case.  Must be created like so:
      # Actor::Builder.new(SingletonActorClass).singleton
      # NO OTHER OPTIONS.  It will fail anyways if you do
      def singleton
        @actor_system.actor_of(
          JavaLib::ClusterSingletonManager.defaultProps(
            @props,@name,JavaLib::PoisonPill.get_instance,nil),'singleton')
        @actor_system.actor_of(
          JavaLib::ClusterSingletonProxy.defaultProps(
            "user/singleton/#{@name}", nil), @name);
      end

      def test_ref
        JavaLib::TestActorRef.create(@actor_system,@props,@name)
      end

      # Sets the parent actor
      # @param parent
      # @return self
      def with_parent(parent)
        @actor_system = parent
        self
      end

      # Sets the actor's name
      # @param name
      # @return self
      def with_name(name)
        @name = name
        self
      end

      def with_dispatcher(name)
        @props = @props.with_dispatcher(name)
        self
      end

      # Run the actor under a router
      # @param router_class [Class] num_router [Integer]
      # @return self
      def with_router(router_class,num_routers)
        @props = @props.with_router(
          router_class.new(num_routers)
        )
        self
      end

      # Creates this router as a distributed router. It will create a group
      # of actors distributed using consistent hashing.  Suggest a hashring size
      # of at least 40 up to 160.
      # @param hashring_size [Integer]
      # @return self
      def distributed(hashring_size)
        @create_hashring = true
        @hashring_size = hashring_size
        self
      end

      # Start the actor(s). If the actor is distributed returns the entire
      # Array of actor refs in the hash ring, otherwise a single actor ref
      # @return actor ref
      def start
        GameMachine.logger.debug "Game actor #{@name} starting"
        if @create_hashring
          hashring = create_hashring(@hashring_size)
          hashring.buckets.map do |bucket_name|
            @actor_system.actor_of(@props, bucket_name)
          end
        else
          @actor_system.actor_of(@props, @name)
        end
      end


      private

      def create_hashring(bucket_count)
        hashring = Hashring.create_actor_ring(@name,bucket_count)
        @klass.add_hashring(@name,hashring)
        hashring
      end

    end
  end
end
