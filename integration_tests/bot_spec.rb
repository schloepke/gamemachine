require 'integration_helper'

module GameMachine

  describe "bot net" do 

    describe "chat client bot" do
      it "starts client" do
        GameMachine::Bot::Client.start('bot',8200)
        sleep 20000
      end
    end

  end
end

