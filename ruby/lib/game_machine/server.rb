module GameMachine
  class Server

    def initialize
      GameMachine.configure_logging
    end

    def stop_udpserver
      if Settings.udp.enabled
        UdpServer.stop
      end
    end

    def stop_udtserver
      if Settings.udt.enabled
        UdtServer.stop
      end
    end

    def start_udpserver
      if Settings.udp.enabled
        UdpServer.start(Settings.udp.host,Settings.udp.port)
      end
    end

    def start_udtserver
      if Settings.udt.enabled
        UdtServer.start(Settings.udt.host,Settings.udt.port)
      end
    end

    def start_actor_system
      GameMachineLoader.new.run('system',
                                Config.akka_config,
                                Settings.game_handler
                               )
    end

    def stop_actor_system
      GameMachineLoader.get_actor_system.shutdown
    end

    def stop
      stop_udpserver
      stop_udtserver
      stop_actor_system
    end

    def start
      start_actor_system
      start_udpserver
      start_udtserver
      start_game_systems
    end

    def start_game_systems
      ActorBuilder.new(Gateway).with_name('Gateway').start
      ActorBuilder.new(LocalEcho).start
      ActorBuilder.new(ConnectionManager).start
      ActorBuilder.new(CommandRouter).with_router(RoundRobinRouter,20).start
    end

  end
end
