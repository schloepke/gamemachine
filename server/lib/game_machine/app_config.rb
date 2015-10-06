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
      JavaLib::AppConfig.set_env_root(ENV['APP_ROOT'])
      JavaLib::AppConfig.set_env(GameMachine.env)
      JavaLib::AppConfig.set_default_game_id(config.default_game_id)
      JavaLib::AppConfig.set_orm(config.orm)
      JavaLib::AppConfig.set_plugins(config.plugins)
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
      JavaLib::AppConfig::Jdbc.set_url(config.jdbc.url)
      
      JavaLib::AppConfig::Couchbase.set_bucket(config.couchbase.bucket)
      JavaLib::AppConfig::Couchbase.set_password(config.couchbase.password)
      JavaLib::AppConfig::Couchbase.set_servers(config.couchbase.servers)
      JavaLib::AppConfig::Datastore.set_store(config.datastore.store)
      JavaLib::AppConfig::Datastore.set_serialization(config.datastore.serialization)
      JavaLib::AppConfig::Datastore.set_cache_write_interval(config.datastore.cache_write_interval)
      JavaLib::AppConfig::Datastore.set_cache_writes_per_second(config.datastore.cache_writes_per_second)
      JavaLib::AppConfig::Datastore.set_mapdb_path(config.datastore.mapdb_path)

      JavaLib::AppConfig::set_game_config(config.game)
            
      JavaLib::AppConfig::Client.setIdleTimeout(config.client.idle_timeout)
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
        config.jdbc.port =      ENV['DB_PORT'].to_i
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
