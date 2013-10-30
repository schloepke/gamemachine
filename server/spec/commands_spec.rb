require 'spec_helper'

module GameMachine
  class CommandTest
    include Commands
  end

  describe Commands do

    subject do
      CommandTest.new
    end

    describe "#commands" do

      it "should be present on any instance that includes Commands module" do
        expect(subject.commands).to be_a(Commands::Base)
      end

      it "should have a chat instance" do
        expect(subject.commands.chat).to be_a(Commands::ChatCommands)
      end

      it "should have a grid instance" do
        expect(subject.commands.grid).to be_a(Commands::GridCommands)
      end

      it "should have a datastore instance" do
        expect(subject.commands.datastore).to be_a(Commands::DatastoreCommands)
      end

      it "should have a player instance" do
        expect(subject.commands.player).to be_a(Commands::PlayerCommands)
      end
    end

  end
end
