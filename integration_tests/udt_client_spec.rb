require 'integration_helper'

module GameMachine

  describe "basic" do 
    let(:client) {Clients::UdtClient.new(:seed01)}

    describe "Sending and receiving messages" do
      it "works" do
        message = Helpers::GameMessage.new('player')
        message.echo_test('hi')
        client.connect
        client.send_message(message.to_byte_array)
        result = client.receive
        logout = Helpers::GameMessage.new('player')
        logout.player_logout
        client.send_message(logout.to_byte_array)
        client.disconnect
      end

    end
  end
end
