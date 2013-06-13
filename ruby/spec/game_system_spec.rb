require 'spec_helper'

module GameMachine
  describe GameSystem do
    before(:each) {Server.new.start_actor_system}
    after(:each) {Server.new.stop_actor_system}

    subject do
      props = Props.new(GameSystem);
      ref = TestActorRef.create(GameMachineLoader.get_actor_system, props, GameSystem.name);
      ref.underlying_actor
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

