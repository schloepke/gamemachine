module GameMachine
  module Models
    class CreateTeam < TeamBase
      attribute :name, String
      attribute :owner, String
      attribute :access, String
    end
  end
end
