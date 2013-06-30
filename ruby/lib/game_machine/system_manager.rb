module GameMachine
  class SystemManager

    class << self
      def registered
        @@registered ||= []
      end

      def register(system_class)
        registered << system_class
        GameMachine.logger.debug "#{system_class} registered"
      end
    end

    def load_user_systems
      verify_app_root!
      GameMachine.logger.debug "APP_ROOT = #{ENV['APP_ROOT']}"
      app_classes = Dir[File.join(ENV['APP_ROOT'], '*.rb')]
      app_classes.each {|app_class| require app_class}
    end

    def verify_app_root!
      unless File.directory?(ENV['APP_ROOT'])
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
        props = JavaLib::Props.new(Protocols::Httpr)
        Server.instance.actor_system.actor_of(props,Protocols::Http.name)
      end

      ActorBuilder.new(ObjectDb).distributed(100).start
      ActorBuilder.new(Pubsub::Subscriber).start
      ActorBuilder.new(SystemMonitor).start
      ActorBuilder.new(ClusterMonitor).start
      ActorBuilder.new(Systems::DefaultHandler).with_router(JavaLib::RoundRobinRouter,20).start
      ActorBuilder.new(Scheduler).start
      ActorBuilder.new(Systems::PlayerAuthentication).with_router(JavaLib::RoundRobinRouter,10).start
      ActorBuilder.new(Systems::LocalEcho).with_router(JavaLib::RoundRobinRouter,10).start
      ActorBuilder.new(Systems::RemoteEcho).with_router(JavaLib::RoundRobinRouter,10).start

      ActorBuilder.new(Systems::LocalEcho).with_name('DistributedLocalEcho').distributed(160).start
      ActorBuilder.new(Systems::ConnectionManager).start

      self.class.register(Systems::RemoteEcho)
      self.class.register(Systems::LocalEcho)
      self.class.register(Systems::ConnectionManager)
    end
  end
end
