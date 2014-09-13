require 'singleton'
require_relative 'settings'
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
      return if loaded
      @config = HoconConfig.config
      if ENV['NODE_HOST']
        set_config_from_env
      end
      @loaded = true
    end

    def set_config_from_env
      GameMachine.logger.info "Setting config from ENV"
      config.http.host = ENV['NODE_HOST']
      config.http.port = ENV['WWW_PORT'].to_i

      config.tcp.host = ENV['NODE_HOST']
      config.tcp.port = ENV['TCP_PORT'].to_i

      config.udp.host = ENV['NODE_HOST']
      config.udp.port = ENV['UDP_PORT'].to_i

      config.akka.host = ENV['NODE_HOST']
      config.akka.port = ENV['AKKA_PORT'].to_i

      if ENV['DB_HOST'] && ENV['DB_NAME'] && ENV['DB_USER'] && ENV['DB_PASS']
        GameMachine.logger.info "Found database config in ENV"

        config.jdbc.hostname = ENV['DB_HOST']
        config.jdbc.database = ENV['DB_NAME']
        config.jdbc.username = ENV['DB_USER']
        config.jdbc.password = ENV['DB_PASS']

        jdbc_url = "#{ENV['DB_HOST']}:#{ENV['DB_PORT']}/#{ENV['DB_NAME']}"
        if config.jdbc.url.match(/mysql/)
          GameMachine.logger.info "Database is Mysql"
          config.jdbc.url = "jdbc:mysql://#{jdbc_url}"
        elsif config.jdbc_url.match(/postgres/)
          GameMachine.logger.info "Database is Postgresql"
          config.jdbc.url = "jdbc:postgresql://#{jdbc_url}"
        end
      end

    end

    private

  end
end
