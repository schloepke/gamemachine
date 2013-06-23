require 'yaml'
require 'rjack-logback'
require 'java'
require 'benchmark'
require 'socket'
require 'settingslogic'
require 'spoon'
require 'consistent_hashing'


jars = Dir[File.join(File.dirname(__FILE__), '../java_lib', '*.jar')]
jars.each {|jar| require jar}

require_relative 'game_machine/java_lib'

java_import 'com.game_machine.entity_system.generated.ObjectdbGet'
java_import 'com.game_machine.entity_system.generated.ObjectdbUpdate'
java_import 'com.game_machine.entity_system.generated.Entity'
java_import 'com.game_machine.entity_system.generated.EntityList'
java_import 'com.game_machine.entity_system.generated.GameCommand'
java_import 'com.game_machine.entity_system.generated.PlayerConnection'
java_import 'com.game_machine.entity_system.generated.ChatMessage'
java_import 'com.game_machine.entity_system.generated.Player'
java_import 'com.game_machine.entity_system.generated.PlayersAroundMe'
java_import 'com.game_machine.entity_system.generated.ClientId'
java_import 'com.game_machine.entity_system.generated.ObjectdbPut'


unless ENV['GAME_ENV']
  ENV['GAME_ENV'] = 'development'
end

module GameMachine
  def self.env
    ENV['GAME_ENV']
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

require_relative 'game_machine/settings'
require_relative 'game_machine/actor_system'
require_relative 'game_machine/actor_factory'
require_relative 'game_machine/hashring'
require_relative 'game_machine/udt_server_actor'
require_relative 'game_machine/game_actor'
require_relative 'game_machine/game_actor_ref'
require_relative 'game_machine/server'
require_relative 'game_machine/command_router'
require_relative 'game_machine/client'
require_relative 'game_machine/local_echo'
require_relative 'game_machine/remote_echo'
require_relative 'game_machine/connection_manager'
require_relative 'game_machine/actor_builder'
require_relative 'game_machine/object_db'
require_relative 'game_machine/udp_server_actor'
require_relative 'game_machine/system_monitor'
require_relative 'game_machine/client_message'

app_classes = Dir[File.join(File.dirname(__FILE__), '../app', '*.rb')]
app_classes.each {|app_class| require app_class}
puts "GAME_ENV is #{GameMachine.env}"
