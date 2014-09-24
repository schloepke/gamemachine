
module GameMachine
  
  class JavaLogger
    [:info, :error, :warn, :fatal, :debug].each do |name|
      define_method(name) do |msg|
        JavaLib::GameMachineLoader.logger.send(name.to_sym,msg)
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
