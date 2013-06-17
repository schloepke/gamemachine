require 'spec_helper'

module GameMachine
  describe GameSystem do

    subject do
      props = Props.new(GameSystem);
      ref = TestActorRef.create(GameMachineLoader.get_actor_system, props, GameSystem.name);
      ref.underlying_actor
    end

    describe "#tell" do
      it "should tell" do
        LocalEcho.tell("test", :server => 'default', :sender => nil)
      end
    end
    describe "#hashring=" do
      it "should set the hashring" do
        GameSystem.hashring = 'test'
        GameSystem.hashring.should == 'test'
        GameSystem.hashrings[GameSystem.name].should == 'test'
      end
    end

  end
end

