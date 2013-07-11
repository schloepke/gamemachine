module GameMachine
  module Logger

    LOGGER = RJack::SLF4J[ 'game_machine' ]

    def configure_logging
      RJack::Logback.configure do
        console = RJack::Logback::ConsoleAppender.new do |a|

        end
        RJack::Logback.root.add_appender( console )
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
