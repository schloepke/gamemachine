module GameMachine
  module Models
    class User < GameMachine::Model
      attribute :id, String
      attribute :password, String
      attribute :authtoken, String
      
      set_id_scope :user
    end
  end
end