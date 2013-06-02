require 'spec_helper'

module GameMachine
  describe CommandRouter do
    before(:all) do
      GameMachine.start
    end
    after(:all) do
      GameMachine.stop
    end

    describe "Authentication" do
      it "should do something" do
        GameMachine::LocalEcho.tell('test')
      end
    end


  end
end
