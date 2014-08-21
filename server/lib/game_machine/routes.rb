module GameMachine
  class Routes
    include Singleton
    attr_reader :game_message_routes, :entity_routes


    def self.game_messages(&block)
      instance.instance_eval(&block)
    end

    def initialize
      @game_message_routes = {}
      @entity_routes = {}
    end

    def route(id, params)
      game_message_routes[id] = params
      if params[:name]
        game_message_routes[params[:name]] = params
      end
    end
  end
end