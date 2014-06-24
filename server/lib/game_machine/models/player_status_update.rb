module GameMachine
  module Models
    class PlayerStatusUpdate < Model
      attribute :player_id, String
      attribute :status, String
    end
  end
end
