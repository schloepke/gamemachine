require 'singleton'
module GameMachine
  class Akka
    include Singleton
    extend Forwardable

    attr_reader :hashring, :address, :app_config

    def self.address
      host = AppConfig.instance.config.akka.host
      port = AppConfig.instance.config.akka.port
      "akka.tcp://#{Akka.instance.config_name}@#{host}:#{port}"
    end

    def initialize!
      @app_config = AppConfig.instance
      @address = self.class.address
      @hashring = JavaLib::Hashring.new('servers',[address],3)
    end

    def init_cluster!(address)
      @address = address
      @hashring = JavaLib::Hashring.new('servers',[address],3)
    end

    def actor_system
      @actor_system.actor_system
    end

    def config_name
      'cluster'
    end

    def akka_config
      config = load_akka_config(config_name)
      config = set_address(config)
      config = set_seeds(config)
    end

    def start
      @actor_system = Actor::System.new(config_name,akka_config)
      @actor_system.create!
      JavaLib::GameMachineLoader.new.run(actor_system)
      if Application.config.seeds.size >= 1
        Application.config.seeds.each do |seed|
          host,port = seed.split(':')
          GameMachine.logger.info "JOINING REMOTE #{host} #{port}"
          JavaLib::ActorUtil.joinCluster("akka.tcp", config_name, host, port.to_i)
        end
      else
        GameMachine.logger.info "JOINING SELF"
        JavaLib::ActorUtil.joinCluster("akka.tcp", config_name, app_config.config.akka.host, app_config.config.akka.port)
      end
    end

    def stop
      @actor_system.shutdown!
    end

    private

    def set_address(config)
      config.sub!('HOST',app_config.config.akka.host)
      config.sub!('PORT',app_config.config.akka.port.to_s)
      config
    end

    def set_seeds(config)
      seeds = Application.config.seeds.map do |seed|
        host,port = seed.split(':')
        "\"akka.tcp://cluster@#{host}:#{port}\""
      end
      config.sub!('SEEDS',seeds.join(','))
      config
    end

    def load_akka_config(name)
      File.read(File.expand_path(File.join(
          GameMachine.app_root,"config/#{name}.conf")
        )
      )
    end

    def start_camel_extension
      if app_config.config.http_enabled
        camel = JavaLib::CamelExtension.get(Akka.instance.actor_system)
        camel_context = camel.context
      end
    end

  end
end
