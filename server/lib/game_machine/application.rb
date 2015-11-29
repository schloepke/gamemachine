module GameMachine
  class Application

    class << self

      def initialize!
        AppConfig.instance.load_config
        JavaLib::GameMachineLoader.preStart
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
        orm_connect

        start_actor_system
        data_store
        
        start_endpoints
        start_core_systems
        start_handlers

        start_game_systems
        load_plugins
       
        start_http

        GameMachine.logger.info("Game Machine start successful")
        
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
            config.jdbc.password || '',
            config.jdbc.driver,
            config.jdbc.url
          )
            GameMachine.logger.error "Unable to establish database connection, exiting"
            System.exit 1
          end
        end
      end

      def load_plugins
        require_relative '../../java/src/main/java/plugins/plugins.rb'

        JavaLib::GameMachineLoader.start_java_game_actors
      end

      def start_http
        http = NetLib::HttpServer.new(config.http.host,config.http.port,config.http.ssl,'message_gateway')
        http.start
      end

     
      def start_endpoints
        if config.tcp.enabled
          NetLib::TcpServer.start(config.tcp.host, config.tcp.port);
          GameMachine.logger.info(
            "Tcp starting on #{config.tcp.host}:#{config.tcp.port}"
          )
        end

        if config.udp.enabled
          NetLib::UdpServer.start("netty",config.udp.host,config.udp.port)
        end

        JavaLib::GameMachineLoader.start_incoming(config.routers.incoming)
      end

      def start_handlers
        JavaLib::GameMachineLoader.start_request_handler(config.routers.request_handler)
      end


      # TODO configurize router sizes
      def start_core_systems
        Actor::Builder.new(ClusterMonitor).start
        Actor::Builder.new(ObjectDb).distributed(1).start
        JavaLib::GameMachineLoader.startObjectDb(config.routers.objectdb)
        Actor::Builder.new(MessageQueue).start

        ClientManager.get_map('preload')
        Actor::Builder.new(ClientManager).with_router(JavaLib::RoundRobinRouter,config.routers.game_handler * 2).start
        Actor::Builder.new(ClientManagerUpdater).start
        
        # Client manager especially needs to be running now, so other actors can
        # register to it to receive events
        unless GameMachine.env == 'test'
          GameMachine.logger.info("Waiting 2 seconds for core actors to start")
          sleep 2
        end

      end

      def start_game_systems
        Actor::Builder.new(GameSystems::Devnull).start
        JavaLib::GameMachineLoader.StartEntityTracking
        Actor::Builder.new(GameSystems::LocalEcho).with_router(JavaLib::RoundRobinRouter,1).start
        Actor::Builder.new(GameSystems::LocalEcho).with_name('DistributedLocalEcho').distributed(2).start
        Actor::Builder.new(GameSystems::StressTest).with_router(JavaLib::RoundRobinRouter,1).start
        Actor::Builder.new(GameSystems::ChatManager).start

        # Make sure shared hashmaps are initialized
        #team_handler = Application.config.handlers.team.constantize.new
        #Actor::Builder.new(GameSystems::TeamManager).with_router(JavaLib::RoundRobinRouter,config.routers.game_handler).start
      end

    end
  end
end
