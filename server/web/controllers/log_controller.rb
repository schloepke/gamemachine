module Web
  module Controllers
    class LogController < BaseController

      attr_reader :logfiles
      def initialize
        logpath = File.join(GameMachine.app_root,'log')
        @logfiles = {
          :app => @applog = File.join(logpath,"#{GameMachine.env}.log"),
          :stdout => @stdout = File.join(logpath,'game_machine.stdout'),
          :stderr => @stderr = File.join(logpath,'game_machine.stderr')
        }
      end

      def readlog(logtype)
        count = 100
        path = logfiles[logtype]
        unless File.exists?(path)
          GameMachine.logger.info "No such logfile: #{path}"
          return []
        end

        tmpname = "/tmp/#{logtype}_tmp"
        system("tail -#{count} #{path} >#{tmpname}")
        IO.readlines(tmpname)
      end

      def logs(logtype)
        readlog(logtype).map do |line|
          if logtype == :app
            line.match(/^(.*?)\[(.*?)\]\s(INFO|ERROR|DEBUG|WARN)\s(.*)/)
            # timestamp,severity,service,message
            [$1,$3.to_sym,$2,$4]
          else
            line
          end
        end
      end

    end
  end
end
