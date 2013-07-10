require 'spec_helper'

module GameMachine
  module Actor

    describe Base do

      let(:aspect) {['one','two']}

      subject do
        props = JavaLib::Props.new(Actor::Base);
        ref = JavaLib::TestActorRef.create(Akka.instance.actor_system, props, Actor::Base.name);
        ref.underlying_actor
      end

      describe "#onReceive" do
        it "forwards the message to on_receive" do
          expect(subject).to receive(:on_receive).with('test')
          subject.onReceive('test')
        end
      end

      describe "#aspect" do
        it "adds the aspects" do
          Actor::Base.aspect(aspect)
          expect(Actor::Base.aspects.first).to eq(aspect)
        end

        it "registeres the class in the system manager" do
          Actor::Base.aspect(aspect)
          expect(Application.registered.include?(Actor::Base)).to be_true
        end
      end

      describe "#sender" do
        it "returns an actor ref" do
          subject.sender.should be_kind_of(Actor::Ref)
        end
      end

      describe "#find_distributed" do
        it "should return ref with correctly formatted path" do
          Actor::Builder.new(GameSystems::LocalEcho).distributed(1).with_name('echotest').start
          actor_ref = GameSystems::LocalEcho.find_distributed('testid','echotest')
          actor_ref.path.match(/akka.tcp:\/\/cluster@localhost:2551\/user\/echotest[\d]{1,2}/).should be_true
        end
      end

      describe "#find_remote" do
        it "should return ref with correctly formatted path" do
          actor_ref = GameSystems::LocalEcho.find_remote('default','test')
          actor_ref.path.match(/akka.tcp:\/\/cluster@localhost:2551\/user\/test/).should be_true
        end
      end

      describe "#find" do
        it "should return ref with correctly formatted path" do
          actor_ref = GameSystems::LocalEcho.find('test')
          actor_ref.path.match(/\/user\/test/).should be_true
        end
      end

      describe "#add_hashring" do
        it "adds the hashring and returns it" do
          expect(Actor::Base.add_hashring('test','ring')).to eq(Actor::Base.hashring('test'))
          Actor::Base.hashring('test').should == 'ring'
        end
      end

    end
  end
end

