
ENV['GAME_ENV'] = 'test'
require 'rubygems'

begin
  require 'game_machine'
rescue LoadError
  require_relative '../lib/game_machine'
end
module GameMachine

  describe 'tcp client' do 

    let(:player) do
      Player.new.set_id('player').set_name('player').
        set_authtoken('authorized')
    end

    let(:echo_test) do
      EchoTest.new.set_message('testing')
    end

    let(:entity) do
      Entity.new.set_id('1').set_echo_test(echo_test)
    end

    let(:client_message) do
      ClientMessage.new.add_entity(entity).
        set_player(player)
    end

    let(:message) do
      String.from_java_bytes(client_message.to_byte_array)
    end

    let(:client) {Clients::TcpClient.new(:seed01)}


    describe "sending and receiving messages" do
      it "should receive reply" do
        10.times do
        client.send_message(message)
        if bytes = client.receive_message.to_java_bytes
          client_message = ClientMessage.parse_from(bytes)
          #puts client_message.client_connection.id
          #puts client_message.client_connection.gateway
        end
        end
      end
    end
  end
end
