require 'integration_helper'

module GameMachine

  describe "chat client" do 

    let(:client) {Client.new(:seed01)}

    let(:chat_actor) do
      GameMachine::Clients::ChatClient.find
    end

    describe "chat client bot" do
      it "starts client" do
        players = (0..200).to_a
        group1 = players[0..98]
        group2 = players[100..-1]
        GameMachine::Botnet::Master.start('botnet1',8200,players,group1)
        #GameMachine::Botnet::Master.start('botnet2',8204,players,group2)
        sleep 20000
      end
    end

  end
end

