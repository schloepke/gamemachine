module Game
  module Models
    class ClanProfile < GameMachine::Model
      attribute :name, String
      attribute :rating, String
      attribute :members, Array
    end
  end
end
