require 'integration_helper'

module GameMachine

  describe "chat client" do 

    let(:client) {Client.new(:seed01)}

    let(:chat_actor) do
      GameMachine::Clients::ChatClient.find
    end

    describe "chat client bot" do
      it "starts client" do
        GameMachine::Botnet::Master.start
        sleep 20000
      end
    end

  end
end

