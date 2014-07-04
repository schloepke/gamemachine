module Game
  module Models
    class ClanMember < GameMachine::Model
      attribute :name, String
      attribute :rating, String
    end
  end
end
