require 'rjack-logback'

module GameMachine
  module Logger

    LOGGER = RJack::SLF4J[ 'game_machine' ]

    def stdout(message)
      print "#{message}\n"
    end

    def logfile
      File.join(ENV['APP_ROOT'], 'log',"#{ENV['GAME_ENV']}.log")
    end

    def configure_logging
      RJack::Logback.configure do
        unless ENV['GAME_ENV'] == 'test'
          console = RJack::Logback::ConsoleAppender.new do |a|
          end
        end

        file = RJack::Logback::FileAppender.new(logfile) do |a|
        end

        RJack::Logback.root.add_appender( file )
        RJack::Logback.root.level = RJack::Logback::INFO
      end
    end

    def logger
      LOGGER
    end

  end
end

GameMachine.send(:extend, GameMachine::Logger)
GameMachine.configure_logging
