
ENV['APP_ROOT'] ||= File.expand_path(Dir.pwd)
ENV['GAME_ENV'] ||= 'development'

module GameMachine
  def self.env
    ENV.fetch('GAME_ENV')
  end

  def self.app_root
    ENV.fetch('APP_ROOT')
  end
end

require 'java'

jars = Dir[File.join(GameMachine.app_root, 'java/lib', '*.jar')]
jars.each {|jar| require jar}

java_import 'com.game_machine.core.net.client.UdtClient'
java_import 'com.game_machine.core.net.client.UdtClientHandler'

require_relative 'game_machine/java_lib'

require_relative 'game_machine/version'
require_relative 'game_machine/message_buffer'
require_relative 'game_machine/vector'
require_relative 'game_machine/logger'
require_relative 'game_machine/settings'
require_relative 'game_machine/helpers/state_machine'
require_relative 'game_machine/actor'
require_relative 'game_machine/commands'
require_relative 'game_machine/handlers/authentication'
require_relative 'game_machine/hashring'
require_relative 'game_machine/application'
require_relative 'game_machine/game_systems'
require_relative 'game_machine/handlers/request'
require_relative 'game_machine/handlers/game'
require_relative 'game_machine/actor/builder'
require_relative 'game_machine/message_queue'
require_relative 'game_machine/player_gateway'
require_relative 'game_machine/game_data'
require_relative 'game_machine/object_db'
require_relative 'game_machine/write_behind_cache'
require_relative 'game_machine/data_store'
require_relative 'game_machine/auth_handlers/base'
require_relative 'game_machine/auth_handlers/basic'
require_relative 'game_machine/system_monitor'
require_relative 'game_machine/cluster_monitor'
require_relative 'game_machine/scheduler'
require_relative 'game_machine/runner'
require_relative 'game_machine/endpoints'
require_relative 'game_machine/protobuf_extensions/entity_helper'
require_relative 'game_machine/helpers/game_message'
require_relative 'game_machine/player_registry'
require_relative 'game_machine/grid_replicator'
require_relative 'game_machine/akka'
require_relative 'game_machine/clients'
require_relative 'game_machine/navigation'

java.util.concurrent.TimeUnit::MILLISECONDS
java.util.concurrent.TimeUnit::SECONDS


