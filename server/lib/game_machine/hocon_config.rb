require 'java'
require File.join(ENV['JAVA_ROOT'], 'lib', 'config-1.2.1.jar')
require 'fileutils'

module GameMachine
  class HoconConfig

    def self.config
      java_import 'com.typesafe.config.Config'
      java_import 'com.typesafe.config.ConfigFactory'
      file = File.join(ENV['APP_ROOT'],'config',"#{ENV['GAME_ENV']}.conf")
      GameMachine.logger.info("Config file is #{file}")
      data = File.read(file)
      config = ConfigFactory.parseString(data).getConfig('gamemachine')
      top = [:handlers, :routers, :jdbc, :datastore, :gamecloud, :grids, :couchbase, :http, :udp, :tcp, :akka, :admin, :regions, :client]
      conf = OpenStruct.new
      top.each {|t| conf.send("#{t}=",OpenStruct.new)}

      conf.plugins = config.get_string_list('plugins').to_a
      conf.default_game_id = config.get_string('default_game_id')
      conf.environment = config.get_string('environment')
      conf.use_regions = config.get_boolean('use_regions')
      conf.orm = config.get_boolean('orm')
      conf.mono_enabled = config.get_boolean('mono_enabled')
      conf.seeds = config.get_string_list('seeds')

      conf.regions = config.get_list('regions').map {|i| i.map(&:unwrapped)}

      conf.client.protocol = config.get_string('client.protocol')
      conf.client.idle_timeout = config.get_int('client.idle_timeout')

      conf.handlers.team = config.get_string('handlers.team')
      conf.handlers.auth = config.get_string('handlers.auth')

      conf.routers.game_handler = config.get_int('routers.game_handler')
      conf.routers.request_handler = config.get_int('routers.request_handler')
      conf.routers.incoming = config.get_int('routers.incoming')
      conf.routers.objectdb = config.get_int('routers.objectdb')

      conf.datastore.store = config.get_string('datastore.store')
      conf.datastore.serialization = config.get_string('datastore.serialization')
      conf.datastore.cache_write_interval = config.get_int('datastore.cache_write_interval')
      conf.datastore.cache_writes_per_second = config.get_int('datastore.cache_writes_per_second')
      conf.datastore.mapdb_path =  File.join(ENV['APP_ROOT'],'db','mapdb')
      
      conf.gamecloud.host = config.get_string('gamecloud.host')
      conf.gamecloud.user = config.get_string('gamecloud.user')
      conf.gamecloud.api_key = config.get_string('gamecloud.api_key')

      conf.game = config.get_config('game')
      
      conf.couchbase.bucket = config.get_string('couchbase.bucket')
      conf.couchbase.password = config.get_string('couchbase.password')
      conf.couchbase.servers = config.get_string_list('couchbase.servers').to_a

      conf.jdbc.hostname = config.get_string('jdbc.hostname')
      conf.jdbc.port = config.get_int('jdbc.port')
      conf.jdbc.database = config.get_string('jdbc.database')
      conf.jdbc.url = config.get_string('jdbc.url')
      conf.jdbc.ds = config.get_string('jdbc.ds')
      conf.jdbc.driver = config.get_string('jdbc.driver')
      conf.jdbc.username = config.get_string('jdbc.username')
      conf.jdbc.password = config.get_string('jdbc.password')

      conf.http.enabled = config.get_boolean('http.enabled')
      conf.http.host = config.get_string('http.host')
      conf.http.port = config.get_int('http.port')
      conf.http.ssl = config.get_boolean('http.ssl')
      
      conf.tcp.enabled = config.get_boolean('tcp.enabled')
      conf.tcp.host = config.get_string('tcp.host')
      conf.tcp.port = config.get_int('tcp.port')

      conf.udp.enabled = config.get_boolean('udp.enabled')
      conf.udp.host = config.get_string('udp.host')
      conf.udp.port = config.get_int('udp.port')

      conf.akka.host = config.get_string('akka.host')
      conf.akka.port = config.get_int('akka.port')

      conf.admin.user = config.get_string('admin.user')
      conf.admin.pass = config.get_string('admin.pass')
      conf
    end
  end
end