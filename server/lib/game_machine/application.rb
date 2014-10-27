module GameMachine
  class Application

    class << self

      def initialize!
        AppConfig.instance.load_config
        akka.initialize!
      end

      def data_store
        store = DbLib::Store.get_instance
        store.connect(
          config.datastore.store,
          config.datastore.serialization
        )
      end

      def akka
        Akka.instance
      end

      def config
        AppConfig.instance.config
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
        DbLib::Store.get_instance.shutdown
      end

      def start
        JavaLib::AuthorizedPlayers.setPlayerAuthentication(Handlers::PlayerAuthentication.instance)
        create_grids

        start_actor_system
        data_store
        orm_connect
        start_endpoints
        start_core_systems
        start_handlers

        if GameMachine.env == 'development'
          start_development_systems
        end

        start_game_systems
        load_games
        start_mono

        
        GameMachine.logger.info("Game Machine start successful")
        
        # This call blocks, make it the last thing we do
        if config.http.enabled
          Thread.new do
            start_http
          end
        end
      end

      def orm_connect
        if config.orm
          pool = GameMachine::JavaLib::DbConnectionPool.getInstance
          unless pool.connect(
            'game_machine_orm',
            config.jdbc.hostname,
            config.jdbc.port,
            config.jdbc.database,
            config.jdbc.ds,
            config.jdbc.username,
            config.jdbc.password || ''
          )
            GameMachine.logger.error "Unable to establish database connection, exiting"
            System.exit 1
          end
        end
      end

      def create_grids
        Grid.load_from_config
      end

      def load_games
        require_relative '../../games/routes.rb'
        require_relative '../../games/boot.rb'
      end

      def start_http
        require_relative '../../web/app'
      end

      def start_mono
        if config.mono_enabled
          GameMachine.logger.info "Starting mono server"
          MonoServer.new.run!
        end
      end

      def start_endpoints
        if config.tcp.enabled
          JavaLib::TcpServer.start(config.tcp.host, config.tcp.port);
          GameMachine.logger.info(
            "Tcp starting on #{config.tcp.host}:#{config.tcp.port}"
          )
        end

        if config.udp.enabled
          JavaLib::UdpServer.start(config.udp.host,config.udp.port)
        end

        JavaLib::GameMachineLoader.start_incoming(config.routers.incoming)
      end

      def start_handlers
        JavaLib::GameMachineLoader.start_request_handler(config.routers.request_handler)
        JavaLib::GameMachineLoader.start_game_handler(config.routers.request_handler)
        
      end

      def start_development_systems
      end

      # TODO configurize router sizes
      def start_core_systems
        Actor::Builder.new(ClusterMonitor).start
        Actor::Builder.new(ObjectDb).distributed(1).start
        JavaLib::GameMachineLoader.startObjectDb(config.routers.objectdb)
        Actor::Builder.new(MessageQueue).start
        Actor::Builder.new(ClientManager).with_router(JavaLib::RoundRobinRouter,config.routers.game_handler * 2).start
        Actor::Builder.new(ClientManagerUpdater).start
        
        # Client manager especially needs to be running now, so other actors can
        # register to it to receive events
        unless GameMachine.env == 'test'
          GameMachine.logger.info("Waiting 2 seconds for core actors to start")
          sleep 2
        end

        JavaLib::GameMachineLoader.StartMessageGateway
        Actor::Builder.new(GameSystems::RemoteEcho).with_router(JavaLib::RoundRobinRouter,config.routers.game_handler).start

        # Mostly unused
        if config.datastore.store == 'gamecloud'
          Actor::Builder.new(CloudUpdater).start
        end

        Actor::Builder.new(SystemStats).start
        Actor::Builder.new(Scheduler).start
        Actor::Builder.new(SystemMonitor).start

        if config.use_regions
          # Our cluster singleton for managing regions
          Actor::Builder.new(GameSystems::RegionManager).singleton

          # Hands out current region info to clients/other actors
          Actor::Builder.new(GameSystems::RegionService).start
        end

        if ENV['CLUSTER_TEST']
          Actor::Builder.new(Killswitch).start
          GameMachine.logger.warn "Killswitch activated"
        end
      end

      def start_game_systems
        Actor::Builder.new(GameSystems::Devnull).start#.with_router(JavaLib::RoundRobinRouter,4).start
        JavaLib::GameMachineLoader.StartEntityTracking
        Actor::Builder.new(GameSystems::LocalEcho).with_router(JavaLib::RoundRobinRouter,1).start
        Actor::Builder.new(GameSystems::LocalEcho).with_name('DistributedLocalEcho').distributed(2).start
        Actor::Builder.new(GameSystems::StressTest).with_router(JavaLib::RoundRobinRouter,1).start
        Actor::Builder.new(GameSystems::ChatManager).start
        Actor::Builder.new(GameSystems::TeamManager).with_router(JavaLib::RoundRobinRouter,config.routers.game_handler).start
        Actor::Builder.new(GameSystems::UserMessageTest).start
      end

    end
  end
end
