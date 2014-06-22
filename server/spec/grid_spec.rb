require 'spec_helper'
module GameMachine
  describe Grid do

    subject {Grid}

    before(:each) do 
      Grid.reset_grids
    end

    describe "#update_freqency_for" do
      it "returns the update freqency for the named grid" do
        Grid.load_from_config
        expect(Grid.update_frequency_for('default')).to eq 1
      end
    end

    describe "#load_from_config" do
      it "should create grids from config" do
        Grid.load_from_config
        expect(Grid.config.has_key?('default')).to be_true
      end
    end

    describe "#find_or_create" do
      it "creates a grid if none exists for the given name" do
        grid = subject.find_or_create('test')
        expect(grid).to be_instance_of(JavaLib::Grid)
      end

      it "returns the same instance if already created" do
        grid = subject.find_or_create('test')
        expect(subject.find_or_create('test')).to eql(grid)
      end
    end
  end
end
