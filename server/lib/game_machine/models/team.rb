module GameMachine
  module Models
    class Team < TeamBase
      attribute :name, String
      attribute :owner, String
      attribute :access, String
      attribute :members, Array
      attribute :invite_id, String
    end
  end
end
