module GameMachine

  class DuplicateHashringError < StandardError;end

  class GameSystem < UntypedActor
    class << self
      alias_method :apply, :new
      alias_method :create, :new

      
      def systems
        [GameMachine::CommandRouter,GameMachine::LocalEcho,GameMachine::ConnectionManager].freeze
      end

      def components
        []
      end

      def hashrings
        @@hashrings ||= java.util.concurrent.ConcurrentHashMap.new
      end

      def hashring
        hashrings.fetch(self.name)
      end

      def hashring=(hashring)
        raise DuplicateHashringError if hashrings[self.name]
        hashrings[self.name] = hashring
      end

      def actor_system
        GameMachineLoader.get_actor_system
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

    def onReceive(message)
      on_receive(message)
    end

    def on_receive(message)
      unhandled(message)
    end

  end
end
