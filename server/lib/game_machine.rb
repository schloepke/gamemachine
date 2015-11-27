require 'rbconfig'

require_relative 'game_machine/ruby_extensions/nilclass'
require_relative 'game_machine/ruby_extensions/string'
require_relative 'game_machine/securerandom'

module GameMachine
  def self.env
    ENV.fetch('GAME_ENV')
  end

  def self.app_root
    ENV.fetch('APP_ROOT')
  end

  def self.java_root
    ENV.fetch('JAVA_ROOT')
  end
end

require 'java'

jars = Dir[File.join(GameMachine.java_root, 'lib', '*.jar')]
jars.each do |jar|
  require jar
end

require_relative 'game_machine/java_lib'
require_relative 'game_machine/gcache'
require_relative 'game_machine/protobuf'
require_relative 'game_machine/version'
require_relative 'game_machine/message_buffer'
require_relative 'game_machine/vector'
require_relative 'game_machine/logger'
require_relative 'game_machine/hocon_config'
require_relative 'game_machine/app_config'
require_relative 'game_machine/helpers/state_machine'
require_relative 'game_machine/actor'
require_relative 'game_machine/commands'
require_relative 'game_machine/model'
require_relative 'game_machine/models'
require_relative 'game_machine/handlers/authentication'
require_relative 'game_machine/application'
require_relative 'game_machine/game_systems'
require_relative 'game_machine/handlers/player_authentication'
require_relative 'game_machine/actor/builder'
require_relative 'game_machine/message_queue'
require_relative 'game_machine/cluster_monitor'
require_relative 'game_machine/object_db'
require_relative 'game_machine/client_manager'
require_relative 'game_machine/protobuf_extensions/entity_helper'
require_relative 'game_machine/helpers/game_message'
require_relative 'game_machine/akka'
require_relative 'game_machine/clients'
require_relative 'game_machine/mono_server'
require_relative 'game_machine/uniqueid'
require_relative 'game_machine/routes'
require_relative 'game_machine/default_handlers'

require_relative 'game_machine/codeblocks'
require_relative 'game_machine/http_helper'


java.util.concurrent.TimeUnit::MILLISECONDS
java.util.concurrent.TimeUnit::SECONDS

Signal.trap("TERM") {
  puts "Caught SIGTERM, exiting"
  System.exit(0)
}
