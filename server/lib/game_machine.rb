
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
java_import 'com.game_machine.entity_system.generated.TrackEntity'
java_import 'com.game_machine.entity_system.generated.GetNeighbors'
java_import 'com.game_machine.entity_system.generated.IsNpc'
java_import 'com.game_machine.entity_system.generated.DestroySingleton'
java_import 'com.game_machine.entity_system.generated.CreateSingleton'
java_import 'com.game_machine.entity_system.generated.NotifySingleton'
java_import 'com.game_machine.entity_system.generated.EntityList'
java_import 'com.game_machine.entity_system.generated.Attack'
java_import 'com.game_machine.entity_system.generated.CombatAbility'
java_import 'com.game_machine.entity_system.generated.Effect'
java_import 'com.game_machine.entity_system.generated.EffectList'
java_import 'com.game_machine.entity_system.generated.Health'
java_import 'com.game_machine.entity_system.generated.PlayerAuthenticated'



require_relative 'game_machine/version'
require_relative 'game_machine/vector'
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
require_relative 'game_machine/player_gateway'
require_relative 'game_machine/game_systems/chat_manager'
require_relative 'game_machine/game_systems/chat'
require_relative 'game_machine/game_systems/chat_topic'
require_relative 'game_machine/game_systems/singleton_controller'
require_relative 'game_machine/game_systems/singleton_router'
require_relative 'game_machine/game_systems/singleton_manager'
require_relative 'game_machine/game_systems/entity_loader'
require_relative 'game_machine/game_data'
require_relative 'game_machine/game_systems/player_manager'
require_relative 'game_machine/object_db'
require_relative 'game_machine/write_behind_cache'
require_relative 'game_machine/data_stores/memory'
require_relative 'game_machine/data_stores/couchbase'
require_relative 'game_machine/data_stores/mapdb'
require_relative 'game_machine/data_store'
require_relative 'game_machine/auth_handlers/base'
require_relative 'game_machine/auth_handlers/basic'
require_relative 'game_machine/system_monitor'
require_relative 'game_machine/cluster_monitor'
require_relative 'game_machine/scheduler'
require_relative 'game_machine/runner'
require_relative 'game_machine/endpoints/tcp_handler'
require_relative 'game_machine/endpoints/udt'
require_relative 'game_machine/endpoints/udp'
require_relative 'game_machine/endpoints/tcp'
require_relative 'game_machine/endpoints/http/auth'
require_relative 'game_machine/protobuf_extensions/client_message_sender'
require_relative 'game_machine/helpers/game_message'
require_relative 'game_machine/player_registry'
require_relative 'game_machine/grid_replicator'
require_relative 'game_machine/akka'
require_relative 'game_machine/clients/client'
require_relative 'game_machine/clients/udt_client'
require_relative 'game_machine/clients/tcp_client'
require_relative 'game_machine/clients/udp_client'

require_relative 'game_machine/bot/client'
require_relative 'game_machine/bot/chat'
require_relative 'game_machine/navigation/detour'
require_relative 'game_machine/navigation/detour_navmesh'
require_relative 'game_machine/navigation/detour_path'
require_relative 'game_machine/navigation/path'

java.util.concurrent.TimeUnit::MILLISECONDS
java.util.concurrent.TimeUnit::SECONDS


if GameMachine::Settings.mono_enabled
  require_relative 'game_machine/mono_test'
  GameMachine::MonoLib.init_mono
end

