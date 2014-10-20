require 'singleton'
require_relative 'hocon_config'
module GameMachine
  class AppConfig
    include Singleton

    attr_reader :config, :loaded

    def initialize
      @loaded = false
      @config = OpenStruct.new
    end

    def load_config
      @config = HoconConfig.config
      set_config_from_env

      set_java_config
      @loaded = true
    end

    def set_java_config
      JavaLib::AppConfig.set_env(GameMachine.env)
      JavaLib::AppConfig::Handlers.setAuth(config.handlers.auth)
      JavaLib::AppConfig::Gamecloud.set_host(config.gamecloud.host)
      JavaLib::AppConfig::Gamecloud.set_user(config.gamecloud.user)
      JavaLib::AppConfig::Gamecloud.set_api_key(config.gamecloud.api_key)

      JavaLib::AppConfig::Jdbc.set_hostname(config.jdbc.hostname)
      JavaLib::AppConfig::Jdbc.set_port(config.jdbc.port)
      JavaLib::AppConfig::Jdbc.set_database(config.jdbc.database)
      JavaLib::AppConfig::Jdbc.set_username(config.jdbc.username)
      JavaLib::AppConfig::Jdbc.set_password(config.jdbc.password)
      JavaLib::AppConfig::Jdbc.set_ds(config.jdbc.ds)
      JavaLib::AppConfig::Jdbc.set_driver(config.jdbc.driver)

      JavaLib::AppConfig::Couchbase.set_bucket(config.couchbase.bucket)
      JavaLib::AppConfig::Couchbase.set_password(config.couchbase.password)
      JavaLib::AppConfig::Couchbase.set_servers(config.couchbase.servers)
    end

    def set_config_from_env
      GameMachine.logger.info "Setting config from ENV"

      if ENV['NODE_HOST']
        config.http.host = ENV['NODE_HOST']
        config.tcp.host = ENV['NODE_HOST']
        config.udp.host = ENV['NODE_HOST']
        config.akka.host = ENV['NODE_HOST']
      end

      if ENV['WWW_PORT']
        config.http.port = ENV['WWW_PORT'].to_i
      end

      if ENV['TCP_PORT']
        config.tcp.port = ENV['TCP_PORT'].to_i
      end

      if ENV['UDP_PORT']
        config.udp.port = ENV['UDP_PORT'].to_i
      end

      if ENV['AKKA_PORT']
        config.akka.port = ENV['AKKA_PORT'].to_i
      end

      if ENV['CLOUD_HOST'] && ENV['CLOUD_USER'] && ENV['API_KEY']
        GameMachine.logger.info "Found gamecloud config in ENV"
        config.gamecloud.host = ENV['CLOUD_HOST']
        config.gamecloud.user = ENV['CLOUD_USER']
        config.gamecloud.api_key = ENV['API_KEY']
      end

      if ENV['DB_HOST'] && ENV['DB_PORT']  && ENV['DB_NAME'] && ENV['DB_USER'] && ENV['DB_PASS']
        GameMachine.logger.info "Found database config in ENV"
        config.jdbc.hostname =  ENV['DB_HOST']
        config.jdbc.port =      ENV['DB_PORT']
        config.jdbc.database =  ENV['DB_NAME']
        config.jdbc.username =  ENV['DB_USER']
        config.jdbc.password =  ENV['DB_PASS']
      end

      if ENV['AKKA_SEED_HOST'] && ENV['AKKA_SEED_PORT']
        GameMachine.logger.info "Found Seed in ENV"
        Application.config.seeds << "#{ENV['AKKA_SEED_HOST']}:#{ENV['AKKA_SEED_PORT']}"
      end

    end

    private

  end
end
