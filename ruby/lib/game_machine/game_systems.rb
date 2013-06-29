module GameMachine
  class GameSystems

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
        ActorBuilder.new(UdpServerActor).start
      end
      if Server.instance.config.udt.enabled
        ActorBuilder.new(UdtServerActor).start
      end
      
      if Server.instance.config.http_enabled
        props = JavaLib::Props.new(HttpServer)
        Server.instance.actor_system.actor_of(props,HttpServer.name)
      end

      ActorBuilder.new(ObjectDb).distributed(100).start
      ActorBuilder.new(Pubsub::Subscriber).start
      ActorBuilder.new(SystemMonitor).start
      ActorBuilder.new(ClusterMonitor).start
      ActorBuilder.new(DefaultGameHandler).with_router(JavaLib::RoundRobinRouter,20).start
      ActorBuilder.new(Scheduler).start
      ActorBuilder.new(PlayerAuthentication).start

      ActorBuilder.new(LocalEcho).start
      ActorBuilder.new(LocalEcho).with_name('DistributedLocalEcho').distributed(160).start
      ActorBuilder.new(RemoteEcho).start
      ActorBuilder.new(ConnectionManager).start

      GameActor.register_system(GameMachine::RemoteEcho)
      GameActor.register_system(GameMachine::LocalEcho)
      GameActor.register_system(GameMachine::ConnectionManager)
    end
  end
end
