module GameMachine
  module Actor

    class DuplicateHashringError < StandardError;end
    class MissingHashringError < StandardError;end

    class Base < JavaLib::UntypedActor

      class << self
        alias_method :apply, :new
        alias_method :create, :new

        def aspects
          @aspects ||= []
        end
        
        def aspect(new_aspects)
          aspects << new_aspects
          unless Application.registered.include?(self)
            Application.register(self)
          end
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
          Actor::Ref.new(local_path(name),name)
        end

        def find_remote(server,name=self.name)
          Actor::Ref.new(remote_path(server,name),name)
        end

        def find_distributed_local(id,name=self.name)
          ensure_hashring(name)
          Actor::Ref.new(local_distributed_path(id, name),name)
        end

        def find_distributed(id,name=self.name)
          ensure_hashring(name)
          Actor::Ref.new(distributed_path(id, name),name)
        end

        def local_path(name)
          "/user/#{name}"
        end

        private

        def ensure_hashring(name)
          unless hashring(name)
            raise MissingHashringError
          end
        end

        def remote_path(server,name)
          "#{Akka.address_for(server)}/user/#{name}"
        end

        def local_distributed_path(id,name)
          bucket = hashring(name).bucket_for(id)
          "/user/#{bucket}"
        end

        def distributed_path(id,name)
          server = Akka.instance.hashring.bucket_for(id)
          bucket = hashring(name).bucket_for(id)
          "#{server}/user/#{bucket}"
        end

      end

      def onReceive(message)
        GameMachine.logger.debug("#{self.class.name} got #{message}")
        on_receive(message)
      end

      def on_receive(message)
        unhandled(message)
      end

      def sender
        Actor::Ref.new(get_sender)
      end

    end
  end
end
