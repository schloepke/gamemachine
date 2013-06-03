require 'yaml'
require 'rjack-logback'
require 'java'
require 'benchmark'
require 'socket'

dir = File.join(Dir.getwd,'../core/lib').gsub(File::SEPARATOR,File::ALT_SEPARATOR || File::SEPARATOR)
#$CLASSPATH << "#{dir}/"
Dir.entries(dir).each do |jar|
  file = File.join(dir,jar).gsub(File::SEPARATOR,File::ALT_SEPARATOR || File::SEPARATOR)
  unless File.directory?(file)
    #puts "Loading #{file}"
    require file
  end
end

java_import 'java.util.concurrent.ConcurrentHashMap'
java_import 'com.game_machine.core.GameMachineLoader'
java_import 'com.game_machine.core.AskProxy'
java_import 'com.game_machine.core.ActorUtil'
java_import 'akka.actor.ActorRef'
java_import 'akka.actor.ActorSystem'
java_import 'akka.actor.Props'
java_import 'akka.actor.UntypedActor'
java_import 'com.game_machine.core.game.Gateway'
java_import 'com.game_machine.core.GatewayMessage'
java_import 'com.game_machine.core.net.server.UdpServer'
java_import 'com.game_machine.core.net.server.UdtServer'

java_import 'java.net.InetSocketAddress'
java_import 'com.barchart.udt.SocketUDT'
java_import 'com.barchart.udt.TypeUDT'
java_import 'com.game_machine.core.MessageUtil'
java_import 'com.game_machine.entity_system.Entities'
java_import 'com.game_machine.entity_system.generated.Entity'
java_import 'com.game_machine.entity_system.generated.Components'
java_import 'com.game_machine.entity_system.generated.GameCommand'
java_import 'com.game_machine.entity_system.generated.PlayerConnection'
java_import 'com.game_machine.entity_system.generated.ChatMessage'
java_import 'com.game_machine.entity_system.generated.Player'
java_import 'com.game_machine.entity_system.generated.PlayersAroundMe'
java_import 'com.game_machine.core.game.Gateway'
java_import 'akka.routing.RoundRobinRouter'
java_import 'com.game_machine.core.net.client.Client'
java_import 'com.typesafe.config.Config'
java_import 'com.typesafe.config.ConfigFactory'
java_import 'com.game_machine.core.persistence.ObjectDb'
java_import 'com.game_machine.core.persistence.Query'
java_import 'com.game_machine.core.persistence.QueryCallable'
java_import 'java.util.concurrent.atomic.AtomicInteger'
java_import 'akka.testkit.TestActorRef'

require 'game_machine/game_message'
require 'game_machine/config'
require 'game_machine/actor_base'
require 'game_machine/server'
require 'game_machine/command_router'
require 'game_machine/client'
require 'game_machine/proxy_actor'
require 'game_machine/local_echo'
require 'game_machine/connection_manager'
require 'game_machine/systems'


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

puts "GAME_ENV is #{GameMachine.env}"
