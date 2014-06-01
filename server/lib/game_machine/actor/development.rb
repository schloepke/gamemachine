module GameMachine
  module Actor
    class Development < Base

      class << self

        def reload_on_change
          Reloadable.register(self.name)
          @reload_on_change = true
        end

        def reload_on_change?
          @reload_on_change ? true : false
        end
      end

      def kill_self(reason)
        raise "Killing #{self}: #{reason}"
      end

      def onReceive(message)
        if message == 'reload'
          kill_self("Actor code change")
        else
          on_receive(message)
        end
      end
    end
  end
end
