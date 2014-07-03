module GameMachine
  module Models
    class Match < Model
      attribute :server, String
      attribute :game_handler, String
    end
  end
end
