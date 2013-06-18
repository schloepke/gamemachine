require 'spec_helper'

module GameMachine
  describe GameActor do

    subject do
      props = Props.new(GameActor);
      ref = TestActorRef.create(GameMachineLoader.get_actor_system, props, GameActor.name);
      ref.underlying_actor
    end

    describe "#find_distributed" do
      it "should return ref with correctly formatted path" do
        ActorBuilder.new(LocalEcho).distributed(1).with_name('echotest').start
        actor_ref = LocalEcho.find_distributed('testid','echotest')
        actor_ref.path.match(/akka.tcp:\/\/system@localhost:2552\/user\/echotest[\d]{1,2}/).should be_true
      end
    end

    describe "#find_remote" do
      it "should return ref with correctly formatted path" do
        actor_ref = LocalEcho.find_remote('default','test')
        actor_ref.path.match(/akka.tcp:\/\/system@localhost:2552\/user\/test/).should be_true
      end
    end

    describe "#find" do
      it "should return ref with correctly formatted path" do
        actor_ref = LocalEcho.find('test')
        actor_ref.path.match(/\/user\/test/).should be_true
      end
    end

    describe "#add_hashring" do
      it "should add the hashring" do
        GameActor.add_hashring('test','ring')
        GameActor.hashring('test').should == 'ring'
      end
    end

  end
end

