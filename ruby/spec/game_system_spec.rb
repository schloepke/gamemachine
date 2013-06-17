require 'spec_helper'

module GameMachine
  describe GameSystem do

    subject do
      props = Props.new(GameSystem);
      ref = TestActorRef.create(GameMachineLoader.get_actor_system, props, GameSystem.name);
      ref.underlying_actor
    end

    describe "#distributed_path" do
      it "should return correctly formatted path" do
        ActorBuilder.new(LocalEcho).distributed(1).with_name('echotest').start
        path = LocalEcho.distributed_path('test','echotest')
        path.match(/akka.tcp:\/\/system@localhost:2552\/user\/echotest[\d]{1,2}/).should be_true
      end
    end

    describe "#remote_path" do
      it "should return correctly formatted path" do
        path = LocalEcho.remote_path('default','test')
        path.match(/akka.tcp:\/\/system@localhost:2552\/user\/test/).should be_true
      end
    end

    describe "#local_path" do
      it "should return correctly formatted path" do
        path = LocalEcho.local_path('test')
        path.match(/\/user\/test/).should be_true
      end
    end
    describe "#tell" do
      it "should tell" do
        LocalEcho.tell("test", :server => 'default', :sender => nil)
      end

      it "server option should create remote path" do
      end

      it "key option should create distributed path" do
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

