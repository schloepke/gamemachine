
java_import 'akka.util.Timeout'

module GameMachine

  class DuplicateHashringError < StandardError;end
  class MissingHashringError < StandardError;end

  class GameActor < UntypedActor

    class << self
      alias_method :apply, :new
      alias_method :create, :new

      def systems
        [GameMachine::RemoteEcho,GameMachine::CommandRouter,GameMachine::LocalEcho,GameMachine::ConnectionManager].freeze
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

      def hashring(name)
        hashrings.fetch(name,nil)
      end

      def add_hashring(name,hashring)
        if hashring(name)
          raise DuplicateHashringError, "name=#{name}"
        end
        hashrings[name] = hashring
      end

      def find(name=self.name)
        GameActorRef.new(local_path(name))
      end

      def find_remote(server,name=self.name)
        GameActorRef.new(remote_path(server,name))
      end

      def find_distributed(id,name=self.name)
        if hashring(name)
          GameActorRef.new(distributed_path(id, name))
        else
          raise MissingHashringError
        end
      end

      def remote_base_uri(server)
        "akka.tcp://system@#{Settings.servers.send(server).akka.host}:#{Settings.servers.send(server).akka.port}"
      end

      def remote_path(server,name)
        "#{remote_base_uri(server)}/user/#{name}"
      end

      def distributed_path(id,name)
        server = hashring(name).server_for(id)
        bucket = hashring(name).bucket_for(id)
        remote_path(server,bucket)
      end

      def local_path(name)
        "/user/#{name}"
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
