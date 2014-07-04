module GameMachine
  module Models
    class Match < Model
      attribute :teams, String
      attribute :server, String
      attribute :game_handler, String
    end
  end
end
