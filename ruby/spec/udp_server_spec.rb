require 'spec_helper'

module GameMachine
  describe UdpServer do
    before(:all) do
      Server.new.start
    end

    after(:all) do
      Server.new.stop
    end
    it "starts the server"
  end
end

