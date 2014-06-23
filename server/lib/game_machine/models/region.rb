module GameMachine
  module Models
    class Region < Model
      attribute :name, String
      attribute :server, String
      attribute :manager, String
    end
  end
end
