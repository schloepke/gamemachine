module GameMachine
  class GameMessage

    attr_reader :client_id, :entity

    def initialize(client_id,entity)
      @client_id = client_id
      @entity = entity
    end
    
  end
end
