module GameMachine
  module Actor
    module Development

      def self.included(base)
        Reloadable.register(base.name)
        base.extend(ClassMethods)
      end

      module ClassMethods

        def reload_on_change?
          true
        end
      end

      def kill_self(reason)
        raise "Killing #{self}: #{reason}"
      end

      def onReceive(message)
        if message == 'reload_because_changed'
          kill_self("Actor code change")
        else
          on_receive(message)
        end
      end
    end
  end
end
