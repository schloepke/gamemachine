module GameMachine
  class ClientMessage

    attr_reader :data, :client_id

    def initialize(data,client_id)
      @data = data
      @client_id = client_id
    end

    def send_to_client
      GameActor.find(client_id.get_gateway).send_message(self)
    end
  end
end
