
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

java_import 'com.game_machine.entity_system.generated.Vector3'
java_import 'com.game_machine.entity_system.generated.Quaternion'
java_import 'com.game_machine.entity_system.generated.Transform'
java_import 'com.game_machine.core.net.client.UdtClient'
java_import 'com.game_machine.core.net.client.UdtClientHandler'
java_import 'com.game_machine.entity_system.generated.ObjectdbGet'
java_import 'com.game_machine.entity_system.generated.ObjectdbUpdate'
java_import 'com.game_machine.entity_system.generated.Entity'
java_import 'com.game_machine.entity_system.generated.ClientMessage'
java_import 'com.game_machine.entity_system.generated.GameCommand'
java_import 'com.game_machine.entity_system.generated.PlayerLogin'
java_import 'com.game_machine.entity_system.generated.ChatMessage'
java_import 'com.game_machine.entity_system.generated.ChatRegister'
java_import 'com.game_machine.entity_system.generated.Player'
java_import 'com.game_machine.entity_system.generated.Neighbors'
java_import 'com.game_machine.entity_system.generated.ClientConnection'
java_import 'com.game_machine.entity_system.generated.ObjectdbPut'
java_import 'com.game_machine.entity_system.generated.EchoTest'
java_import 'com.game_machine.entity_system.generated.Publish'
java_import 'com.game_machine.entity_system.generated.Subscribe'
java_import 'com.game_machine.entity_system.generated.Unsubscribe'
java_import 'com.game_machine.entity_system.generated.ChatChannel'
java_import 'com.game_machine.entity_system.generated.ChatChannels'
java_import 'com.game_machine.entity_system.generated.ErrorMessage'
java_import 'com.game_machine.entity_system.generated.JoinChat'
java_import 'com.game_machine.entity_system.generated.LeaveChat'
java_import 'com.game_machine.entity_system.generated.ClientDisconnect'
java_import 'com.game_machine.entity_system.generated.Disconnected'
java_import 'com.game_machine.entity_system.generated.PlayerLogout'
java_import 'com.game_machine.entity_system.generated.ErrorMessage'
java_import 'com.game_machine.entity_system.generated.PlayerRegister'
java_import 'com.game_machine.entity_system.generated.RegisterPlayerObserver'
java_import 'com.game_machine.entity_system.generated.PlayerMove'
java_import 'com.game_machine.entity_system.generated.PlayerAttack'
java_import 'com.game_machine.entity_system.generated.Target'
java_import 'com.game_machine.entity_system.generated.TrackEntity'
java_import 'com.game_machine.entity_system.generated.GetNeighbors'
java_import 'com.game_machine.entity_system.generated.Npc'
java_import 'com.game_machine.entity_system.generated.CreateNpc'



require_relative 'game_machine/version'
require_relative 'game_machine/dyn4j'
require_relative 'game_machine/java_lib'
require_relative 'game_machine/logger'
require_relative 'game_machine/settings'
require_relative 'game_machine/helpers/state_machine'
require_relative 'game_machine/actor/system'
require_relative 'game_machine/actor/base'
require_relative 'game_machine/actor/factory'
require_relative 'game_machine/actor/ref'
require_relative 'game_machine/handlers/authentication'
require_relative 'game_machine/hashring'
require_relative 'game_machine/application'
require_relative 'game_machine/handlers/request'
require_relative 'game_machine/handlers/game'
require_relative 'game_machine/game_systems/entity_tracking'
require_relative 'game_machine/game_systems/local_echo'
require_relative 'game_machine/game_systems/remote_echo'
require_relative 'game_machine/actor/builder'
require_relative 'game_machine/message_queue'
require_relative 'game_machine/game_systems/chat_manager'
require_relative 'game_machine/game_systems/chat'
require_relative 'game_machine/game_systems/chat_topic'
require_relative 'game_machine/object_db'
require_relative 'game_machine/write_behind_cache'
require_relative 'game_machine/data_stores/memory'
require_relative 'game_machine/data_stores/couchbase'
require_relative 'game_machine/data_store'
require_relative 'game_machine/system_monitor'
require_relative 'game_machine/cluster_monitor'
require_relative 'game_machine/scheduler'
require_relative 'game_machine/daemon'
require_relative 'game_machine/endpoints/udt'
require_relative 'game_machine/endpoints/udp'
require_relative 'game_machine/endpoints/http/auth'
require_relative 'game_machine/protobuf_extensions/client_message_sender'
require_relative 'game_machine/helpers/game_message'
require_relative 'game_machine/player_registry'
require_relative 'game_machine/akka'
require_relative 'game_machine/physics/grid'
require_relative 'game_machine/clients/client'
require_relative 'game_machine/clients/udt_client'

require_relative 'game_machine/bot/client'
require_relative 'game_machine/bot/chat'

java.util.concurrent.TimeUnit::MILLISECONDS
java.util.concurrent.TimeUnit::SECONDS

if GameMachine::Settings.mono_enabled
  require_relative 'game_machine/mono_test'
  GameMachine::MonoLib.init_mono
end

if ENV['START_SERVER']
  GameMachine::Application.initialize!(ENV['SERVER_NAME'] || 'default',true)
  GameMachine::Application.start
end

if ENV['STOP_SERVER']
  GameMachine::Akka.instance.init!
  GameMachine::Akka.instance.kill_all
end

