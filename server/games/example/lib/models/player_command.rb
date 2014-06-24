module Example
  module Models
    class PlayerCommand < GameMachine::Model
      attribute :command, String
    end
  end
end
