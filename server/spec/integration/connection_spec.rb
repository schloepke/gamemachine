require 'spec_helper_minimal'
require_relative '../../lib/game_machine/clients/test_client'
require_relative '../../lib/game_machine/console'
module GameMachine

  describe "connections" do

    it "multiple client connections" do
      clients = {}
      count = 10

      count.times do |i|
        name = "user#{i}"
        clients[i] = Clients::TestClient.new(name,'seed')
      end

      count.times do |i|
        clients[i].login
      end

      
      count.times do |i|
        clients[i].logout
      end
      
    end



    xit "private chat messages between nodes" do
      player1 = Clients::TestClient.new('player1','seed')
      player1.login

      player2 = Clients::TestClient.new('player2','node1')
      player2.login

      player3 = Clients::TestClient.new('player3','node2')
      player3.login

      10.times do
      player1.send_chat_message('private','player2','hiya player2')
      player2.send_chat_message('private','player1','hiya player1')
      player3.send_chat_message('private','player1','hiya player1')

      entity = player2.receive_message
      expect(entity.has_chat_message).to be_truthy
      expect(entity.chat_message.message).to eql('hiya player2')

      entity = player1.receive_message
      expect(entity.has_chat_message).to be_truthy
      expect(entity.chat_message.message).to eql('hiya player1')

      entity = player1.receive_message
      expect(entity.has_chat_message).to be_truthy
      expect(entity.chat_message.message).to eql('hiya player1')
    end

      player1.logout
      player2.logout
      player3.logout
    end
  end
end