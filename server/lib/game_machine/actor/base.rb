module GameMachine
  module Actor

    class DuplicateHashringError < StandardError;end
    class MissingHashringError < StandardError;end

    # @abstract All game actors inherit fromm this class
    class Base < JavaLib::UntypedActor
  
      @@player_controller = nil

      class << self
        alias_method :apply, :new
        alias_method :create, :new


        # Sets the system wide player controller class.
        # When a player logs in, a player controller with this class
        # will be created. The system notifies the player controller when
        # various player lifecycle events happen.
        #
        # This should only be called on subclasses, never on the Actor base
        # class
        def set_player_controller
          @@player_controller = self
          GameMachine.logger.info("Player controller set to #{self.name}")
        end

        def player_controller
          @@player_controller
        end


        def aspects
          @aspects ||= []
        end
        
        # Sets the message types that this actor knows about. Can be called
        # multiple times.  If passed an array of more then one message type,
        # both message types will need to be present on an entity before the
        # system will route the entity to the actor.
        #
        # messages will be routed to actors based on the aspects it has
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

        # Find a local actor by name
        # @return [Actor::Ref]
        def find(name=self.name)
          Actor::Ref.new(local_path(name),name)
        end

        # Find a remote actor by name
        # @return [Actor::Ref]
        def find_remote(server,name=self.name)
          Actor::Ref.new(remote_path(server,name),name)
        end

        # Returns a local actor ref from the distributed ring of actors based
        # on a consistent hashing of the id.
        # @return [Actor::Ref]
        def find_distributed_local(id,name=self.name)
          ensure_hashring(name)
          Actor::Ref.new(local_distributed_path(id, name),name)
        end

        # Returns an actor ref from the distributed ring of actors based
        # on a consistent hashing of the id. The actor returned can be from
        # any server in the cluster
        # @return [Actor::Ref]
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
