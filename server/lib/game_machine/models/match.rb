module GameMachine
  module Models
    class Match < TeamBase
      attribute :teams, String
      attribute :server, String
      attribute :game_handler, String
    end
  end
end
