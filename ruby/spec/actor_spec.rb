require 'spec_helper'

module GameMachine
  describe Actor do

    subject do
      props = JavaLib::Props.new(Actor);
      ref = JavaLib::TestActorRef.create(Server.instance.actor_system, props, Actor.name);
      ref.underlying_actor
    end

    describe "#sender" do

      it "returns an actor ref" do
        subject.sender.should be_kind_of(ActorRef)
      end
    end

    describe "#find_distributed" do
      it "should return ref with correctly formatted path" do
        ActorBuilder.new(Systems::LocalEcho).distributed(1).with_name('echotest').start
        actor_ref = Systems::LocalEcho.find_distributed('testid','echotest')
        actor_ref.path.match(/akka.tcp:\/\/cluster@localhost:2551\/user\/echotest[\d]{1,2}/).should be_true
      end
    end

    describe "#find_remote" do
      it "should return ref with correctly formatted path" do
        actor_ref = Systems::LocalEcho.find_remote('default','test')
        actor_ref.path.match(/akka.tcp:\/\/cluster@localhost:2551\/user\/test/).should be_true
      end
    end

    describe "#find" do
      it "should return ref with correctly formatted path" do
        actor_ref = Systems::LocalEcho.find('test')
        actor_ref.path.match(/\/user\/test/).should be_true
      end
    end

    describe "#add_hashring" do
      it "should add the hashring" do
        Actor.add_hashring('test','ring')
        Actor.hashring('test').should == 'ring'
      end
    end

  end
end

