
module GameMachine
  class Akka
    include Singleton

    attr_reader :name, :cluster, :hashring, :address

    def self.address_for(server)
      "akka.tcp://#{Akka.instance.config_name}@#{Settings.servers.send(server).akka.host}:#{Settings.servers.send(server).akka.port}"
    end

    def initialize!(name, cluster)
      @name = name
      @cluster = cluster
      @address = self.class.address_for(@name)
      @hashring = Hashring.new([@address])
    end

    def init_cluster!(address)
      @address = address
      @hashring = Hashring.new([address])
    end

    def cluster?
      cluster ? true : false
    end

    def actor_system
      @actor_system.actor_system
    end

    def config_name
      @cluster ? 'cluster' : 'system'
    end

    def akka_config
      @cluster ? akka_cluster_config : akka_server_config
    end

    def start
      @actor_system = Actor::System.new(config_name,akka_config)
      @actor_system.create!
      JavaLib::GameMachineLoader.new.run(actor_system,Settings.game_handler)
      start_camel_extension
    end

    def stop
      @actor_system.shutdown!
      Actor::Base.reset_hashrings
    end

    private

    def set_address(config)
      config.sub!('HOST',akka_host)
      config.sub!('PORT',akka_port.to_s)
      config
    end

    def set_seeds(config)
      seeds = Settings.seeds.map do |seed| 
        seed_host = Settings.servers.send(seed).akka.host
        seed_port = Settings.servers.send(seed).akka.port
        "\"akka.tcp://cluster@#{seed_host}:#{seed_port}\""
      end
      config.sub!('SEEDS',seeds.join(','))
      config
    end

    def akka_cluster_config
      config = load_akka_config('cluster')
      config = set_address(config)
      config = set_seeds(config)
    end

    def akka_server_config
      config = load_akka_config('akka')
      config = set_address(config)
    end

    def load_akka_config(name)
      File.read(File.expand_path(File.join(
          GameMachine.app_root,"config/#{name}.conf")
        )
      )
    end

    def akka_host
      Application.config.akka_host
    end

    def akka_port
      Application.config.akka_port
    end

    def start_camel_extension
      if Application.config.http_enabled
        camel = JavaLib::CamelExtension.get(Akka.instance.actor_system)
        camel_context = camel.context
      end
    end

  end
end
