
module GameMachine
  
  class JavaLogger
    java_import 'org.slf4j.Logger'
    java_import 'org.slf4j.LoggerFactory'

    attr_reader :default_logger
    def initialize
      @default_logger = create('default')
    end

    def create(name)
      LoggerFactory.getLogger(name)
    end

    [:info, :error, :warn, :fatal, :debug].each do |name|
      define_method(name) do |msg|
        default_logger.send(name.to_sym,msg)
      end
    end
  end

  module Logger
    LOGGER = JavaLogger.new

    def logfile
      File.join(ENV['APP_ROOT'], 'log',"#{ENV['GAME_ENV']}.log")
    end

    def logger
      LOGGER
    end
    
  end
end

GameMachine.send(:extend, GameMachine::Logger)
