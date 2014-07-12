require 'rjack-logback'

module GameMachine
  class AdminUiLogger
    [:info, :error, :warn, :fatal].each do |name|
      define_method(name) do |msg|
        log(msg)
      end
    end

    attr_reader :admin_ui
    def initialize
      @admin_ui = GameMachine::JavaLib::AdminUi.getAdminUi
    end

    def debug(msg)

    end
    def log(msg)
      admin_ui.updateLog("#{msg}\n".to_java_string)
    end
  end

  module Logger

    LOGGER = RJack::SLF4J[ 'game_machine' ]

    def admin_ui_logger
      @@admin_ui_logger ||= AdminUiLogger.new
    end

    def stdout(message)
      unless ENV['GAME_ENV'] == 'test'
        print "#{message}\n"
      end
    end

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
      if ENV['ADMIN_UI']
        admin_ui_logger
      else
        LOGGER
      end
    end

  end
end

GameMachine.send(:extend, GameMachine::Logger)
GameMachine.configure_logging
