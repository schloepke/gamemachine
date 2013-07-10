module GameMachine
  class Application

    class << self

      def initialize!(name='default', cluster=false)
        akka.initialize!(name,cluster)
        map_config(name,cluster)
        config.handlers = [Handlers::Request,Handlers::Authentication, Handlers::Game]
      end

      def map_config(name,cluster)
        config.name = name
        config.cluster = cluster
        server = Settings.servers.send(name)
        config.http_enabled = server.http_enabled
        config.udp = server.udp
        config.udt = server.udt
        config.akka_host = server.akka.host
        config.akka_port = server.akka.port
      end

      def start_actor_system
        akka.start
      end

      def stop_actor_system
        akka.stop
      end

      def stop
        akka.stop
        DataStore.instance.shutdown
      end

      def start
        akka.start
        DataStore.instance
        start_game_systems
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

      def load_user_systems
        GameMachine.logger.debug "APP_ROOT = #{GameMachine.app_root}"
        app_classes = Dir[File.join(GameMachine.app_root,'app','lib', '*.rb')]
        app_classes.each {|app_class| require app_class}
      end

      def start_game_systems
        if config.udp.enabled
          Actor::Builder.new(Endpoints::Udp).start
        end
        if config.udt.enabled
          Actor::Builder.new(Endpoints::Udt).start
          JavaLib::UdtServer.start(config.udt.host,config.udt.port)
        end
        
        if config.http_enabled
          props = JavaLib::Props.new(Endpoints::Http::Auth)
          Akka.instance.actor_system.actor_of(props,Endpoints::Http::Auth.name)
        end

        Actor::Builder.new(PlayerRegistry).start
        Actor::Builder.new(ObjectDb).distributed(100).start
        Actor::Builder.new(MessageQueue).start
        Actor::Builder.new(SystemMonitor).start
        Actor::Builder.new(ClusterMonitor).start
        Actor::Builder.new(Scheduler).start
        Actor::Builder.new(WriteBehindCache).distributed(100).start

        Actor::Builder.new(Handlers::Request).with_router(JavaLib::RoundRobinRouter,20).start
        Actor::Builder.new(Handlers::Authentication).distributed(160).start
        Actor::Builder.new(Handlers::Game).with_router(JavaLib::RoundRobinRouter,20).start

        Actor::Builder.new(GameSystems::LocalEcho).with_router(JavaLib::RoundRobinRouter,10).start
        Actor::Builder.new(GameSystems::LocalEcho).with_name('DistributedLocalEcho').distributed(160).start
        Actor::Builder.new(GameSystems::RemoteEcho).with_router(JavaLib::RoundRobinRouter,10).start
        Actor::Builder.new(ChatManager).start

        load_user_systems
      end

    end
  end
end
