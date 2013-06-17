require 'singleton'

module GameMachine
  class Server
    include Singleton

    attr_reader :config, :name
    def initialize
      setup_signal_handlers
      GameMachine.configure_logging
      @tmp_dir = '/tmp/game_machine'
      FileUtils.mkdir_p @tmp_dir
    end

    def init!(name='default')
      @name = name
      @config = Settings.servers.send(name)
    end

    def setup_signal_handlers
      Signal.trap('TERM') do
        GameMachine.logger.warn('Caught signal TERM, shutting down')
        shutdown
      end

      Signal.trap("INT") do
        GameMachine.logger.warn('Caught signal INT, shutting down')
        shutdown
      end
    end

    def shutdown
      stop
      System.exit 0
    end

    def pidfile(pid)
      File.join(@tmp_dir,"#{pid}.pid")
    end

    def pidfiles
      Dir[File.join(@tmp_dir, '*.pid')]
    end

    def write_pidfile
      File.open(pidfile($$),'w') {|f| f.write($$)}
    end

    def kill_all
      pidfiles.each do |pidfile|
        pid = File.read(pidfile)
        cmd = "kill -9 #{pid}"
        GameMachine.logger.info cmd
        system(cmd)
        FileUtils.rm pidfile
      end
    end

    def stop_udpserver
      if config.udp.enabled
        UdpServer.stop
      end
    end

    def stop_udtserver
      if config.udt.enabled
        UdtServer.stop
      end
    end

    def start_udpserver
      if config.udp.enabled
        UdpServer.start(config.udp.host,config.udp.port)
      end
    end

    def start_udtserver
      if config.udt.enabled
        UdtServer.start(config.udt.host,config.udt.port)
      end
    end

    def start_actor_system
      GameMachineLoader.new.run('system',
                                Settings.akka_config,
                                Settings.game_handler
                               )
    end

    def stop_actor_system
      GameMachineLoader.get_actor_system.shutdown
      GameMachineLoader.get_actor_system.awaitTermination
      GameSystem.reset_hashrings
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
      write_pidfile
    end

    def start_game_systems
      ActorBuilder.new(ObjectDb).start
      ActorBuilder.new(SystemMonitor).start
      ActorBuilder.new(Gateway).with_name('Gateway').start
      ActorBuilder.new(LocalEcho,'arg1').distributed(160).start
      ActorBuilder.new(ConnectionManager).start
      ActorBuilder.new(CommandRouter).with_router(RoundRobinRouter,20).start
    end

    def to_s
      "#{config.akka.host}:#{config.akka.port}"
    end

  end
end
