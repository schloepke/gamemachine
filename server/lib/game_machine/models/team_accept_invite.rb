module GameMachine
  module Models
    class TeamAcceptInvite < TeamBase
      attribute :name, String
      attribute :invite_id, String
      attribute :invitee, String
    end
  end
end
