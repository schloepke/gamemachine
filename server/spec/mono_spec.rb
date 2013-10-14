require 'spec_helper'
module GameMachine
  describe MonoLib do

    it "can send message to ruby" do
      Actor::Builder.new(Endpoints::ActorUdp).start
      sleep 1
      entity = Entity.new.set_id('test')
      10.times do
        MonoTest.find.tell(entity)
      end
      sleep 2
      puts "TEST COMPLETE"
    end
  end
end
