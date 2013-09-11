require 'integration_helper'
module GameMachine

  describe "chat" do 
      
    before(:all) do
      @client = Clients::TestClient.start('test1',8200)
      @client2 = Clients::TestClient.start('test2',8202)
    end

    after(:all) do
      @client.stop
      @client2.stop
    end

    it "join chat request sends ChatChannels to client" do
      message = Helpers::GameMessage.new('player1')
      message.join_chat('mygroup')
      @client.send_to_server(message.client_message)
      expect(@client.entity_with_component('ChatChannels')).to be_true
    end

    it "receive chat message from other player" do
      client_join_message = Helpers::GameMessage.new('player1')
      client_join_message.join_chat('mygroup')

      client2_join_message = Helpers::GameMessage.new('player2')
      client2_join_message.join_chat('mygroup')

      chat_message = Helpers::GameMessage.new('player1')
      chat_message.chat_message('group','Chat it up!','mygroup')

      @client.send_to_server(client_join_message.client_message)
      expect(@client.entity_with_component('ChatChannels')).to be_true

      @client2.send_to_server(client2_join_message.client_message)
      expect(@client2.entity_with_component('ChatChannels')).to be_true

      @client.send_to_server(chat_message.client_message)
      expect(@client2.entity_with_component('ChatMessage',1)).to be_true
      
    end

  end
end
