require 'yaml'
require 'rjack-logback'
require 'java'
require 'benchmark'
require 'socket'
require 'settingslogic'
require 'spoon'
require 'consistent_hashing'
require 'json'
require 'benchmark'
require 'descriptive_statistics'
require "uri"


jars = Dir[File.join(File.dirname(__FILE__), '../java_lib', '*.jar')]
jars.each {|jar| require jar}

require_relative 'game_machine/java_lib'

java_import 'com.game_machine.entity_system.generated.ObjectdbGet'
java_import 'com.game_machine.entity_system.generated.ObjectdbUpdate'
java_import 'com.game_machine.entity_system.generated.Entity'
java_import 'com.game_machine.entity_system.generated.ClientMessage'
java_import 'com.game_machine.entity_system.generated.GameCommand'
java_import 'com.game_machine.entity_system.generated.PlayerLogin'
java_import 'com.game_machine.entity_system.generated.ChatMessage'
java_import 'com.game_machine.entity_system.generated.Player'
java_import 'com.game_machine.entity_system.generated.PlayersAroundMe'
java_import 'com.game_machine.entity_system.generated.ClientConnection'
java_import 'com.game_machine.entity_system.generated.ObjectdbPut'
java_import 'com.game_machine.entity_system.generated.EchoTest'
java_import 'com.game_machine.entity_system.generated.Publish'
java_import 'com.game_machine.entity_system.generated.Subscribe'
java_import 'com.game_machine.entity_system.generated.Unsubscribe'
java_import 'com.game_machine.entity_system.generated.ChatChannel'
java_import 'com.game_machine.entity_system.generated.JoinChat'
java_import 'com.game_machine.entity_system.generated.LeaveChat'


ENV['APP_ROOT'] ||= File.join(File.dirname(__FILE__), '../app')
ENV['GAME_ENV'] ||= 'development'

module GameMachine
  def self.env
    ENV.fetch('GAME_ENV')
  end

  def self.app_root
    ENV.fetch('APP_ROOT')
  end

  def self.configure_logging
    @@logger = RJack::SLF4J[ 'game_machine' ]

    RJack::Logback.configure do
      console = RJack::Logback::ConsoleAppender.new do |a|

      end
      RJack::Logback.root.add_appender( console )
      RJack::Logback.root.level = RJack::Logback::INFO
    end
  end

  def self.logger
    @@logger
  end
end
GameMachine.configure_logging

require_relative 'game_machine/settings'
require_relative 'game_machine/actor_system'
require_relative 'game_machine/actor_factory'
require_relative 'game_machine/hashring'
require_relative 'game_machine/actor'
require_relative 'game_machine/actor_ref'
require_relative 'game_machine/server'
require_relative 'game_machine/systems/request_handler'
require_relative 'game_machine/systems/entity_dispatcher'
require_relative 'game_machine/client'
require_relative 'game_machine/systems/local_echo'
require_relative 'game_machine/systems/remote_echo'
require_relative 'game_machine/actor_builder'
require_relative 'game_machine/message_queue'
require_relative 'game_machine/chat_manager'
require_relative 'game_machine/systems/chat'
require_relative 'game_machine/object_db'
require_relative 'game_machine/write_behind_cache'
require_relative 'game_machine/data_stores/memory'
require_relative 'game_machine/data_stores/couchbase'
require_relative 'game_machine/data_store'
require_relative 'game_machine/system_monitor'
require_relative 'game_machine/cluster_monitor'
require_relative 'game_machine/scheduler'
require_relative 'game_machine/daemon'
require_relative 'game_machine/system_manager'
require_relative 'game_machine/endpoints/udt'
require_relative 'game_machine/endpoints/udp'
require_relative 'game_machine/endpoints/http/auth'
require_relative 'game_machine/systems/authentication_handler'
require_relative 'game_machine/protobuf_extensions/client_message_sender'


java.util.concurrent.TimeUnit::MILLISECONDS
java.util.concurrent.TimeUnit::SECONDS

