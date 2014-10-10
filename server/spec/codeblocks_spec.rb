require 'spec_helper_minimal'
module GameMachine

  describe Codeblocks do

    let(:username) {'bob'}

    describe "Compiling" do
      it "Should compile user codeblock" do
        code = Codeblocks::Compiler.testcode
        compiler = Codeblocks::Compiler.new(username,code)
        codeblock = compiler.compile
        expect(codeblock).to be_truthy
      end
    end

    describe "Running unrestricted" do
      it "should run" do
        code = Codeblocks::Compiler.testcode
        compiler = Codeblocks::Compiler.new(username,code)
        codeblock = compiler.compile
        executor = JavaLib::CodeblockExecutor.new
        executor.set_perms
        expect(executor.run_unrestricted(codeblock,'testing restricted')).to be_truthy
      end
    end

    describe "Running restricted" do
      it "should fail to run" do
        code = Codeblocks::Compiler.testcode
        compiler = Codeblocks::Compiler.new(username,code)
        codeblock = compiler.compile
        executor = JavaLib::CodeblockExecutor.new
        executor.set_perms
        expect(executor.run_restricted(codeblock,'testing restricted')).to be_falsy
      end
    end
  end
end