module GameMachine
  class Configuration

    attr_accessor :handlers, :cluster, :name
    def initialize
      @cluster = false
      @name = nil
      @handlers = [Handlers::Request,Handlers::Authentication, Handlers::Game]
    end
  end
end
