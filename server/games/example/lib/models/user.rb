module Example
  module Models
    class User < GameMachine::Model
      attribute :id, String
      attribute :password, String
      attribute :authtoken, String
      
      validates :id, length: {maximum: 20, minimum: 4}
      validates :password, length: {maximum: 12, minimum: 4}

      set_id_scope :user
    end
  end
end
