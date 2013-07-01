module GameMachine

  class DuplicateHashringError < StandardError;end
  class MissingHashringError < StandardError;end

  class Actor < JavaLib::UntypedActor

    class << self
      alias_method :apply, :new
      alias_method :create, :new

      def components
        @components ||= Set.new
      end

      def register_component(component)
        components << component
        GameMachine.logger.debug "Component #{component} registered to #{self.name}"
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
        ActorRef.new(local_path(name))
      end

      def find_remote(server,name=self.name)
        ActorRef.new(remote_path(server,name))
      end

      def ensure_hashring(name)
        unless hashring(name)
          raise MissingHashringError
        end
      end

      def find_distributed_local(id,name=self.name)
        ensure_hashring(name)
        ActorRef.new(local_distributed_path(id, name))
      end

      def find_distributed(id,name=self.name)
        ensure_hashring(name)
        ActorRef.new(distributed_path(id, name))
      end

      def remote_path(server,name)
        "#{Server.address_for(server)}/user/#{name}"
      end

      def local_distributed_path(id,name)
        bucket = hashring(name).bucket_for(id)
        "/user/#{bucket}"
      end

      def distributed_path(id,name)
        server = Server.instance.hashring.bucket_for(id)
        bucket = hashring(name).bucket_for(id)
        "#{server}/user/#{bucket}"
      end

      def local_path(name)
        "/user/#{name}"
      end

      def send_to_client(message,client_connection,sender=nil)
        if message.is_a?(Entity)
          client_message = ClientMessage.new
          client_message.add_entity(message)
        elsif message.is_a?(ClientMessage)
          client_message = message
        end
        client_message.set_client_connection(client_connection)
        Actor.find(client_connection.gateway).tell(client_message,sender)
      end
    end

    def send_to_client(message)
      self.class.send_to_client(message,client_connection,self)
    end

    def client_connection
      @client_connection
    end

    def onReceive(message)
      GameMachine.logger.debug("#{self.class.name} got #{message}")
      set_client_connection(message)
      on_receive(message)
    end

    def on_receive(message)
      unhandled(message)
    end

    def sender
      ActorRef.new(get_sender)
    end

    private

    def set_client_connection(message)
      @client_connection = nil
      if message.is_a?(Entity)
        if message.has_client_connection
          @client_connection = message.client_connection
        end
      end
    end

  end
end
