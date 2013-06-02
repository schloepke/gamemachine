module GameMachine
  class ActorBase < UntypedActor
    class << self
      alias_method :apply, :new
      alias_method :create, :new

      def actor_system
        GameMachineLoader.get_actor_system
      end

      def start
        actor_system.actor_of(Props.new(self), self.name)
      end

      def ask(message, &blk)
        AskProxy.ask(message, self.name) do |reply|
          blk.call(reply)
        end
      end

      def tell(message,sender=nil)
        actor_selection = ActorUtil.get_actor_selection_by_name(self.name)
        actor_selection.tell(message,sender)
      end

    end

    def onReceive(message)
      on_receive(message)
    end

    def on_receive(message)
      unhandled(message)
    end

  end
end
