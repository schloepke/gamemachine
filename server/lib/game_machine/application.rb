module GameMachine
  class Application

    class << self

      def initialize!(name='default', cluster=false)
        config.name = name
        config.cluster = cluster
        map_settings

        config.handlers = default_handlers
        config.request_handler_routers = 20
        config.game_handler_routers = 20
        config.authentication_handler_ring_size = 160
        akka.initialize!(config.name,config.cluster)
      end

      def akka
        Akka.instance
      end

      def config
        @config ||= OpenStruct.new
      end

      def registered
        @@registered ||= Set.new
      end

      def register(system_class)
        registered << system_class
        GameMachine.logger.debug "#{system_class} registered"
      end

      def start_actor_system
        akka.start
      end

      def stop_actor_system
        akka.stop
      end

      def stop
        stop_actor_system
        DataStore.instance.shutdown
        JavaLib::UdtServer.stop
      end

      def start
        start_actor_system
        DataStore.instance
        start_endpoints
        start_core_systems
        start_handlers
        start_game_systems
        load_games
        GameMachine.stdout("Game Machine start successful")
      end

      def start_endpoints
        if config.server.tcp_enabled
          Actor::Builder.new(Endpoints::Tcp).start
          GameMachine.stdout(
            "Tcp starting on #{config.server.tcp_host}:#{config.server.tcp_port}"
          )
        end

        if config.server.udp_enabled
          Actor::Builder.new(Endpoints::Udp).start
          GameMachine.stdout(
            "UDP starting on #{config.server.udp_host}:#{config.server.udp_port}"
          )
        end

        if config.server.udt_enabled
          Actor::Builder.new(Endpoints::Udt).start
          JavaLib::UdtServer.start(config.server.udt_host,config.server.udt_port)
          GameMachine.stdout(
            "UDT starting on #{config.server.udt_host}:#{config.server.udt_port}"
          )
        end
        
        if config.server.http_enabled
          props = JavaLib::Props.new(Endpoints::Http::Auth)
          Akka.instance.actor_system.actor_of(props,Endpoints::Http::Auth.name)
        end
      end

      def load_games
        bootfile = File.join(GameMachine.app_root,'boot.rb')
        if File.exists?(bootfile)
          require bootfile
          GameMachine.logger.info "boot.rb loaded"
          GameMachine.stdout "boot.rb loaded"
        else
          GameMachine.logger.info "boot.rb not loaded (not found)"
          GameMachine.stdout "boot.rb not loaded (not found)"
        end
      end

      def start_handlers
        Actor::Builder.new(Handlers::Request).with_router(
          JavaLib::RoundRobinRouter,config.request_handler_routers
        ).start
        Actor::Builder.new(Handlers::Authentication).distributed(
          config.authentication_handler_ring_size
        ).start
        Actor::Builder.new(Handlers::Game).with_router(
          JavaLib::RoundRobinRouter,config.game_handler_routers
        ).start
      end

      def start_core_systems
        Actor::Builder.new(ClusterMonitor).start
        Actor::Builder.new(PlayerGateway).start
        Actor::Builder.new(PlayerRegistry).start
        Actor::Builder.new(ObjectDb).distributed(100).start
        Actor::Builder.new(MessageQueue).start
        Actor::Builder.new(SystemMonitor).start
        Actor::Builder.new(Scheduler).start
        Actor::Builder.new(WriteBehindCache).distributed(200).start
        if Settings.mono_enabled
          Actor::Builder.new(MonoTest).with_router(JavaLib::RoundRobinRouter,10).start
        end
        Actor::Builder.new(GridReplicator).start
        Actor::Builder.new(GameSystems::EntityLoader).start
      end

      def start_game_systems
        Actor::Builder.new(GameSystems::EntityTracking).with_router(JavaLib::RoundRobinRouter,40).start
        Actor::Builder.new(GameSystems::LocalEcho).with_router(JavaLib::RoundRobinRouter,10).start
        Actor::Builder.new(GameSystems::LocalEcho).with_name('DistributedLocalEcho').distributed(10).start
        Actor::Builder.new(GameSystems::RemoteEcho).with_router(JavaLib::RoundRobinRouter,10).start
        Actor::Builder.new(GameSystems::ChatManager).start
        Actor::Builder.new(GameSystems::SingletonManager).start
        Actor::Builder.new(GameSystems::PlayerManager).with_router(JavaLib::RoundRobinRouter,10).start
      end

      private

      def default_handlers
        [
          Handlers::Request,
          Handlers::Authentication,
          Handlers::Game
        ]
      end

      def map_settings
        config.game_handler = Settings.game_handler
        config.login_username = 'player'
        config.authtoken = 'authorized'
        config.data_store = Settings.data_store
        config.seeds = Settings.seeds
        config.servers = Settings.servers
        server = config.servers.send(config.name)
        config.server = server
      end

    end
  end
end
