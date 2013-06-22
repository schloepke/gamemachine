require 'spec_helper'

module GameMachine
  describe GameActorRef do

    let(:local_echo) do
      props = JavaLib::Props.new(LocalEcho);
      ref = JavaLib::TestActorRef.create(Server.instance.actor_system, props, 'localecho1');
      ref.underlying_actor
    end

    subject do
      GameActorRef.new('test')
    end

    describe "#initialize" do
      it "sets the path" do
        GameActorRef.new('test').path.should == 'test'
      end
    end

    describe "#send_message" do
      it "default should call tell with message and nil sender" do
        subject.should_receive(:tell).with('test',nil)
        subject.send_message('test')
      end

      it "blocking=true should call ask with message and default timeout" do
        subject.should_receive(:ask).with('test',100)
        subject.send_message('test', :blocking => true)
      end

      it "sender option should be passed to tell" do
        subject.should_receive(:tell).with('test','sender')
        subject.send_message('test',:sender => 'sender')
      end

      it "timeout option should be passed to ask" do
        subject.should_receive(:ask).with('test',2)
        subject.send_message('test', :timeout => 2, :blocking => true)
      end

      it "actor should get a message" do
        local_echo.should_receive(:on_receive).with('hi')
        ref = LocalEcho.find('localecho1')
        ref.send_message('hi')
      end

      it "should get returned message" do
        ActorBuilder.new(LocalEcho).with_name('echotest2').start
        ref = LocalEcho.find('echotest2')
        ref.send_message('hi', :blocking => true, :timeout => 1000).should == 'hi'
      end

      it "should return false on timeout" do
        ref = LocalEcho.find('blah')
        ref.send_message('hi', :blocking => true, :timeout => 1).should be_false
      end
    end
  end
end
