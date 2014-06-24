module Example
  module Models
    class Vitals < GameMachine::Model
      attribute :max_health, Integer
      attribute :health, Integer
      attribute :id, String
      attribute :defense_skill, Integer
      attribute :offense_skill, Integer
      attribute :entity_type, String
      attribute :x, Integer
      attribute :y, Integer
      attribute :zone, String

      set_id_scope :vitals
    end
  end
end
