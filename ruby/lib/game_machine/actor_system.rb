module GameMachine
  class ActorSystem

    attr_reader :name, :actor_system
    def initialize(name,config_str)
      @config_str = config_str
      @name = name
    end

    def config
		  @config ||= JavaLib::ConfigFactory.parseString(@config_str).getConfig(name).withFallback(JavaLib::ConfigFactory.load)
    end

    def create!
		  @actor_system ||= JavaLib::ActorSystem.create(name, config)
    end

    def shutdown!
      actor_system.shutdown
      actor_system.await_termination
    end

  end
end
