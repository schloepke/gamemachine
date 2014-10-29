module GameMachine
  module Models
    class PlayerTeam < TeamBase
      attribute :id, String
      attribute :name, String
      attribute :match_id, String
    end
  end
end
