module GameMachine
  module Models
    class TeamInvite < TeamBase
      attribute :name, String
      attribute :invite_id, String
    end
  end
end
