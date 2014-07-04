module GameMachine
  module Models
    class PlayerTeam < Model
      attribute :id, String
      attribute :name, String
      attribute :match_id, String
    end
  end
end
