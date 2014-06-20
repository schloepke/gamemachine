module Example
  module Models
    class UserStats < GameMachine::Model
      attribute :health, Integer
      attribute :id, String
      attribute :defense_skill, Integer
      attribute :offense_skill, Integer

      set_id_scope :ustats
    end
  end
end
