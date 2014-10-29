module GameMachine
  module Models
    class DestroyTeam < TeamBase
      attribute :name, String
      attribute :owner, String
    end
  end
end
