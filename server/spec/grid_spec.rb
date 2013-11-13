require 'spec_helper'
module GameMachine
  describe Grid do

    subject {Grid}

    before(:each) do 
      Grid.reset_grids
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
