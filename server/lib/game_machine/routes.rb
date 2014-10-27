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
      distributed = params[:distributed] ? true : false
      game_message_routes[id] = params
      JavaLib::GameMessageRoute.add(id.to_s,params[:name],params[:to],distributed)
      if params[:name]
        JavaLib::GameMessageRoute.add(params[:name],params[:name],params[:to],distributed)
        game_message_routes[params[:name]] = params
      end
    end
  end
end