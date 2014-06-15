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
    end

    private

    def load_settings(name)
      Settings.servers.send(name).each do |key,value|
        config.send("#{key}=",value)
      end
    end

  end
end
