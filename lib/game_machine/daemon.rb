module GameMachine
  class Daemon

    def initialize
      setup_signal_handlers
      @tmp_dir = '/tmp/game_machine'
      FileUtils.mkdir_p @tmp_dir
    end

    def setup_signal_handlers
      Signal.trap('TERM') do
        GameMachine.logger.warn('Caught signal TERM, shutting down')
        shutdown
      end

      #Signal.trap("INT") do
      #  GameMachine.logger.warn('Caught signal INT, shutting down')
      #  shutdown
      #end
    end

    def shutdown
      Server.instance.stop
      GameSystems.exit 0
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
  end
end
