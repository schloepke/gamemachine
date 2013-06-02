module GameMachine
  class Server


    def self.start_netservers
      if GameMachine.env != 'test'
        if Config.config[:udp][:enabled]
          UdpServer.start(Config.config[:udp][:host],Config.config[:udp][:port])
        end
        if Config.config[:udt][:enabled]
          UdtServer.start(Config.config[:udt][:host],Config.config[:udt][:port])
        end
      end
    end

    def self.stop_netservers
      if GameMachine.env != 'test'
        if Config.config[:udp][:enabled]
          UdpServer.stop
        end
        if Config.config[:udt][:enabled]
          UdtServer.stop
        end
      end
    end

    def self.stop
      GameMachineLoader.get_actor_system.shutdown
      stop_netservers
    end

    def self.start
      Config.load
      Config.configure_logging
      if GameMachine.env == 'test'
        GameMachineLoader.new.run_test
      else
        GameMachineLoader.new.run('system',Config.akka_config)
        start_netservers
        load_system
      end



    end

    def self.load_system
      actor_system = GameMachineLoader.get_actor_system
      LocalEcho.start
      ConnectionManager.start
      actor_system.actor_of(Props.new(CommandRouter).withRouter(RoundRobinRouter.new(20)), CommandRouter.name)
    end
  end
end
