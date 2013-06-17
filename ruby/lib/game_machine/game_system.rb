
java_import 'akka.util.Timeout'

module GameMachine

  class DuplicateHashringError < StandardError;end
  class MissingHashringError < StandardError;end

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

      def reset_hashrings
        @@hashrings = nil
      end

      def hashrings
        @@hashrings ||= java.util.concurrent.ConcurrentHashMap.new
      end

      def hashring
        hashrings.fetch(self.name,nil)
      end

      def hashring=(hashring)
        raise DuplicateHashringError if hashrings[self.name]
        hashrings[self.name] = hashring
      end

      def actor_system
        GameMachineLoader.get_actor_system
      end

      def remote_base_uri(server)
        "akka.tcp://system@#{Settings.servers.send(server).akka.host}:#{Settings.servers.send(server).akka.port}"
      end

      def remote_path(server,name)
        "#{remote_base_uri(server)}/user/#{name}"
      end

      def distributed_path(id)
        server = hashring.server_for(id)
        bucket = hashring.bucket_for(id)
        remote_path(server,bucket)
      end

      def local_path(name)
        "/user/#{self.name}"
      end

      def actor_selection(message,options={})
        if options[:key]
          if hashring
            path = distributed_path(options[:key])
          else
            raise MissingHashringError
          end
        elsif options[:server]
          path = remote_path(options[:server],self.name)
        else
          path = local_path(self.name)
        end
        actor_system.actor_selection(path)
      end

      def tell(message,options={})
        actor_selection(message,options).tell(message,options[:sender])
      end

      def ask(message,options={:timeout => 1})
        selection = actor_selection(message,options)
        ref = AskableActorSelection.new(selection)
        future = ref.ask(message,Timeout.new(options[:timeout]))
        Await.result(future, Duration.create(options[:timeout], TimeUnit::MILLISECONDS))
      rescue Java::JavaUtilConcurrent::TimeoutException => e
        GameMachine.logger.warn("TimeoutException caught in ask (timeout = #{options[:timeout]})")
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
