require 'integration_helper'

module GameMachine

  describe "chat client" do 

    let(:client) {Client.new(:seed01)}

    let(:chat_actor) do
      GameMachine::Clients::ChatClient.find
    end

    describe "chat client bot" do
      it "starts client" do
        10.times do |i|
          player_id = "player#{i}"
          GameMachine::Botnet::Master.start(player_id,8200,player_id)
        end
        sleep 20000
      end
    end

  end
end

