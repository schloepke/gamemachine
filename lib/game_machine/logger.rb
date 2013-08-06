module GameMachine
  module Logger

    LOGGER = RJack::SLF4J[ 'game_machine' ]

    def logfile
      File.join(ENV['APP_ROOT'], 'log',"#{ENV['GAME_ENV']}.log")
    end

    def configure_logging
      RJack::Logback.configure do
        console = RJack::Logback::ConsoleAppender.new do |a|
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
