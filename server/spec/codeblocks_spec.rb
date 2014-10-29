require 'spec_helper_minimal'
module GameMachine

  describe Codeblocks do

    let(:username) {'bob'}

    describe "Compiling" do
      it "Should compile user codeblock" do
        code = Codeblocks::Compiler.testcode_pass
        compiler = Codeblocks::Compiler.new(username,code)
        codeblock = compiler.compile
        expect(codeblock).to be_truthy
      end
    end

    describe "Running awake unrestricted" do
      it "should run" do
        code = Codeblocks::Compiler.testcode_pass
        compiler = Codeblocks::Compiler.new(username,code)
        codeblock = compiler.compile
        executor = JavaLib::CodeblockExecutor.new
        executor.set_perms
        expect(executor.run_unrestricted(codeblock,'awake','testing restricted')).to be_truthy
      end
    end

    describe "Running unrestricted" do
      it "should run" do
        code = Codeblocks::Compiler.testcode_fail
        compiler = Codeblocks::Compiler.new(username,code)
        codeblock = compiler.compile
        executor = JavaLib::CodeblockExecutor.new
        executor.set_perms
        expect(executor.run_unrestricted(codeblock,'run','testing restricted')).to be_truthy
      end
    end

    describe "Running restricted" do
      it "should fail to run" do
        code = Codeblocks::Compiler.testcode_fail
        compiler = Codeblocks::Compiler.new(username,code)
        codeblock = compiler.compile
        executor = JavaLib::CodeblockExecutor.new
        executor.set_perms
        expect(executor.run_restricted(codeblock,'run','testing restricted')).to be_falsy
      end
    end

    # The way we load codeblocks doesn't let us do expectactions on the codeblock itself.  Need to figure this out.
    describe "Codeblock actors" do

      xit "should run codeblock when sent message" do
        code = Codeblocks::Compiler.testcode_pass
        compiler = Codeblocks::Compiler.new(username,code)
        codeblock = compiler.compile
        name = "#{username}_test"

        actor_system = JavaLib::ActorSystem.create('system')
        JavaLib::GameMachineLoader.new.run(actor_system)
        ref = JavaLib::TestActorRef.create(actor_system,JavaLib::Props.create(JavaLib::CodeblockActor.java_class,codeblock),name)
        actor = ref.underlying_actor

        expect(codeblock).to receive('run')
        actor.on_receive("testing 1 2 3")
        actor_system.shutdown
        actor_system.await_termination
      end

      xit "Starting a codeblock actor" do
        ref = JavaLib::CodeblockActor.start(name,codeblock)
        JavaLib::ActorUtil.ask(name,"message",1000)
        JavaLib::CodeblockActor.stop(name)
      end
    end
  end
end