require 'integration_helper'

module GameMachine

  describe "bot net" do 

    describe "chat client bot" do
      it "starts client" do
        200.times do |i|
          GameMachine::Bot::Client.start("bot#{i}",8200)
        end
        sleep 20000
      end
    end

  end
end

