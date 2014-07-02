module GameMachine
  module Models
    class CreateTeam < Model
      attribute :name, String
      attribute :owner, String
      attribute :access, String
    end
  end
end
