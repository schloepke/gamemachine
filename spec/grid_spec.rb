require 'spec_helper'
require 'matrix'
module GameMachine
  describe Physics::Grid do

    subject do
      Physics::Grid.new(100,25)
    end

    describe "#set" do
      it "creates a grid point in correct cell" do
        subject.set(1,0,0)
        point = subject.points_in_cell(0).first
        expect(point).to be_a(GameMachine::Physics::GridPoint)
        expect(point.id).to eq(1)
      end
    end

    describe "#get" do
      it "Returns the cell for the given id" do
        subject.set(1,0,0)
        expect(subject.get(1)).to eq (0)
      end
    end

    describe "#remove" do
      it "Removes the cell entry" do
        subject.set(1,0,0)
        subject.remove(1)
        points = subject.points_in_cell(0)
        expect(points).to eq([])
      end
    end

    describe "#neighbors" do

      it "returns empty array when no neighbors found" do
        expect(subject.neighbors(0,0,25)).to eq([])
      end

      it "returns all objects within given radius of cell" do
        subject.set(1,0,0)
        points = subject.neighbors(0,0,25)
        grid_point = points.first
        expect(grid_point.id).to eq(1)
      end

      it "does not return cells out of range" do
        subject.set(1,0,0)
        expect(subject.neighbors(50,50,25)).to eq([])
      end

      it "returns multiple points in the same cell" do
        subject.set(1,0,0)
        subject.set(2,0,0)
        points = subject.neighbors(0,0,25)
        expect(points.first.id).to eq(1)
        expect(points.last.id).to eq(2)
      end
    end
  end
end
