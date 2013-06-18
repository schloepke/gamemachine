module GameMachine

  class DistributedMessage

  end

  class BlockingMessage

  end

  class RemoteMessage

  end

  class GameMessage

    attr_reader :message, :game_system_class

    def initialize(message,game_system_class,options={})
      @message = message
      @game_system_class = game_system_class
      @options = options
    end

    def sender
      @options[:sender]
    end

    def timeout
      @options[:timeout] || 100
    end

    def distributed_hash_key
      @options[:key]
    end

    def blocking?
      @options[:blocking]
    end

    def server
      @options[:server]
    end

    def game_system_name
      @options[:name] || game_system_class.name
    end

    def actor_system
      GameMachineLoader.get_actor_system
    end

    def remote_base_uri
      "akka.tcp://system@#{Settings.servers.send(server).akka.host}:#{Settings.servers.send(server).akka.port}"
    end

    def remote_path
      "#{remote_base_uri(server)}/user/#{name}"
    end

    def distributed_path
      server = hashring(name).server_for(distributed_hash_key)
      bucket = hashring(name).bucket_for(distributed_hash_key)
      remote_path(server,bucket)
    end

    def local_path(name)
      "/user/#{name}"
    end

    def make_path
      if options[:key]
        if hashring(name)
          distributed_path(options[:key], name)
        else
          raise MissingHashringError
        end
      elsif options[:server]
        remote_path(options[:server],name)
      else
        local_path(name)
      end
    end

    def actor_selection
      actor_system.actor_selection(make_path)
    end

    def tell
      actor_selection.tell(message,sender)
    end

    def ask
      duration = Duration.create(timeout, TimeUnit::MILLISECONDS)
      t = Timeout.new(duration)
      ref = AskableActorSelection.new(actor_selection)
      future = ref.ask(message,t)
      Await.result(future, duration)
    rescue Java::JavaUtilConcurrent::TimeoutException => e
      GameMachine.logger.warn("TimeoutException caught in ask (timeout = #{timeout})")
    end

  end
end
