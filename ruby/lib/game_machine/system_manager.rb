module GameMachine
  class SystemManager

    class << self
      def registered
        @@registered ||= Set.new
      end

      def register(system_class)
        registered << system_class
        GameMachine.logger.debug "#{system_class} registered"
      end
    end

    def load_user_systems
      verify_app_root!
      GameMachine.logger.debug "APP_ROOT = #{GameMachine.app_root}"
      app_classes = Dir[File.join(GameMachine.app_root,'lib', '*.rb')]
      app_classes.each {|app_class| require app_class}
    end

    def verify_app_root!
      unless File.directory?(GameMachine.app_root)
        raise StandardError, "APP_ROOT not set!"
      end
    end

    def start
      if Server.instance.config.udp.enabled
        ActorBuilder.new(Protocols::Udp).start
      end
      if Server.instance.config.udt.enabled
        ActorBuilder.new(Protocols::Udt).start
        JavaLib::UdtServer.start(Server.instance.config.udt.host,Server.instance.config.udt.port)
      end
      
      if Server.instance.config.http_enabled
        props = JavaLib::Props.new(Protocols::Http)
        Server.instance.actor_system.actor_of(props,Protocols::Http.name)
      end

      ActorBuilder.new(ObjectDb).distributed(100).start
      ActorBuilder.new(Pubsub::Subscriber).start
      ActorBuilder.new(SystemMonitor).start
      ActorBuilder.new(ClusterMonitor).start
      ActorBuilder.new(Scheduler).start

      ActorBuilder.new(Systems::RequestHandler).with_router(JavaLib::RoundRobinRouter,20).start
      ActorBuilder.new(Systems::AuthenticationHandler).distributed(160).start
      ActorBuilder.new(Systems::EntityDispatcher).with_router(JavaLib::RoundRobinRouter,20).start

      ActorBuilder.new(Systems::LocalEcho).with_router(JavaLib::RoundRobinRouter,10).start
      ActorBuilder.new(Systems::LocalEcho).with_name('DistributedLocalEcho').distributed(160).start
      ActorBuilder.new(Systems::RemoteEcho).with_router(JavaLib::RoundRobinRouter,10).start

      self.class.register(Systems::RemoteEcho)
      self.class.register(Systems::LocalEcho)

      load_user_systems
    end
  end
end
