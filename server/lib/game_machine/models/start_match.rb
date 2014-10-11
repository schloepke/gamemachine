module GameMachine
  module Models
    class StartMatch < TeamBase
      attribute :team_names, Array
      attribute :game_handler, String
    end
  end
end
