module GameMachine
  class ActorBase < UntypedActor
    class << self
      alias_method :apply, :new
      alias_method :create, :new

      def components
        []
      end

      def add_system
        components.each do |component|
          systems[component] ||= []
          systems[component] << self.name
        end
      end

      def actor_system
        GameMachineLoader.get_actor_system
      end

      def start(options={})
        if options[:router]
          props = Props.new(self).with_router(options[:router].new(options[:router_size]))
        else
          props = Props.new(self)
        end
        Systems.register(self,components)
        actor_system.actor_of(props, self.name)
      end

      def ask(message, &blk)
        AskProxy.ask(message, self.name) do |reply|
          blk.call(reply)
        end
      end

      def tell(message,sender=nil)
        actor_selection = ActorUtil.get_selection_by_name(self.name)
        actor_selection.tell(message,sender)
      end

    end

    def initialize
    end

    def onReceive(message)
      on_receive(message)
    end

    def on_receive(message)
      unhandled(message)
    end

  end
end
