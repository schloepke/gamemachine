module GameMachine
  class ClientMessage

    attr_reader :data, :client_connection

    def initialize(data,client_connection)
      @data = data
      @client_connection = client_connection
    end

    def send_to_client
      Actor.find(client_connection.get_gateway).send_message(self)
    end
  end
end
