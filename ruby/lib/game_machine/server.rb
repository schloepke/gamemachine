module GameMachine
  class Server

    def initialize
      Config.load
      GameMachine.configure_logging
    end

    def udp_enabled
      Config.config[:udp][:enabled]
    end

    def udt_enabled
      Config.config[:udt][:enabled]
    end

    def stop_udpserver
      if udp_enabled
        UdpServer.stop
      end
    end

    def stop_udtserver
      if udt_enabled
        UdtServer.stop
      end
    end

    def start_udpserver
      if udp_enabled
        UdpServer.start(Config.config[:udp][:host],Config.config[:udp][:port])
      end
    end

    def start_udtserver
      if udt_enabled
        UdtServer.start(Config.config[:udt][:host],Config.config[:udt][:port])
      end
    end

    def start_actor_system
      GameMachineLoader.new.run('system',
                                Config.akka_config,
                                Config.config[:game_handler]
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
      actor_system = GameMachineLoader.get_actor_system
      props = Props.new(Gateway)
      actor_system.actor_of(props, 'Gateway')

      LocalEcho.start
      ConnectionManager.start
      CommandRouter.start(:router => RoundRobinRouter, :router_size => 20)
    end

  end
end
