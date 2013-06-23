require 'singleton'

module GameMachine
  class Server
    include Singleton

    attr_reader :config, :name, :cluster

    def self.address_for(server)
      "akka.tcp://#{Server.instance.config_name}@#{Settings.servers.send(server).akka.host}:#{Settings.servers.send(server).akka.port}"
    end

    def initialize
      GameMachine.configure_logging
    end

    def init!(name='default', opts={})
      default_opts = {:cluster => false}
      opts = default_opts.merge(opts)
      @name = name
      @config = Settings.servers.send(name)
      @cluster = opts[:cluster]
    end

    def daemon
      @daemon ||= Daemon.new
    end

    def hashring
      @hashring ||= Hashring.new([address])
    end

    def cluster_members
      @cluster_members ||= {}
    end

    def akka_config
      @cluster ? Settings.akka_cluster_config : Settings.akka_config
    end

    def actor_system
      @actor_system.actor_system
    end

    def address
      self.class.address_for(name)
    end

    def cluster?
      cluster ? true : false
    end

    def config_name
      @cluster ? 'cluster' : 'system'
    end

    def start_actor_system
      @actor_system = ActorSystem.new(config_name,akka_config)
      @actor_system.create!
      JavaLib::GameMachineLoader.new.run(actor_system,Settings.game_handler)
    end

    def stop_actor_system
      @actor_system.shutdown!
      GameActor.reset_hashrings
    end

    def stop
      stop_actor_system
    end

    def start
      start_actor_system
      start_game_systems
      daemon.write_pidfile
    end


    def start_game_systems
      GameSystems.new.start
    end

  end
end
