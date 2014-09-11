require 'singleton'
require_relative 'settings'
module GameMachine
  class AppConfig
    include Singleton

    attr_reader :config

    def initialize
      @config = OpenStruct.new
    end

    def reload
      @config = OpenStruct.new
      load_config
    end

    def server_config(server)
      Settings.servers.send(server)
    end

    def set_defaults
      unless config.request_handler_routers
        config.request_handler_routers = 10
      end
      unless config.game_handler_routers
        config.game_handler_routers = 10
      end
      unless config.udp_routers
        config.udp_routers = 10
      end
    end

    def load_config(name)
      config.name = name
      set_defaults
      load_settings(name)
      if ENV['NODE_HOST']
        set_config_from_env
      end
    end

    def set_config_from_env
      GameMachine.logger.info "Setting config from ENV"
      config.http_host = ENV['NODE_HOST']
      config.http_port = ENV['WWW_PORT'].to_i

      config.tcp_host = ENV['NODE_HOST']
      config.tcp_port = ENV['TCP_PORT'].to_i

      config.udp_host = ENV['NODE_HOST']
      config.udp_port = ENV['UDP_PORT'].to_i

      config.akka_host = ENV['NODE_HOST']
      config.akka_port = ENV['AKKA_PORT'].to_i

      if ENV['DB_HOST'] && ENV['DB_NAME'] && ENV['DB_USER'] && ENV['DB_PASS']
        GameMachine.logger.info "Found database config in ENV"

        config.jdbc_hostname = ENV['DB_HOST']
        config.jdbc_database = ENV['DB_NAME']
        config.jdbc_username = ENV['DB_USER']
        config.jdbc_password = ENV['DB_PASS']

        jdbc_url = "#{ENV['DB_HOST']}:#{ENV['DB_PORT']}/#{ENV['DB_NAME']}"
        if config.jdbc_url.match(/mysql/)
          GameMachine.logger.info "Database is Mysql"
          config.jdbc_url = "jdbc:mysql://#{jdbc_url}"
        elsif config.jdbc_url.match(/postgres/)
          GameMachine.logger.info "Database is Postgresql"
          config.jdbc_url = "jdbc:postgresql://#{jdbc_url}"
        end
      end

    end

    private

    def load_settings(name)
      Settings.servers.send(name).each do |key,value|
        config.send("#{key}=",value)
      end
    end

  end
end
