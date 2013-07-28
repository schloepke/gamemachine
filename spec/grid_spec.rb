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
        expect(point[1]).to eq(0)
        expect(point[2]).to eq(0)
      end
    end

    describe "#get" do
      it "Returns the values for the given id" do
        subject.set(1,0,0)
        values = subject.get(1)
        expect(values[0]).to eq(1)
        expect(values[1]).to eq(0)
        expect(values[2]).to eq(0)
        expect(values[3]).to eq(0)
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
        expect(points.first[1]).to eq(0)
      end

      it "does not return cells out of range" do
        subject.set(1,0,0)
        expect(subject.neighbors(50,50,25)).to eq([])
      end

      it "returns multiple points in the same cell" do
        subject.set(1,3,0)
        subject.set(2,4,0)
        points = subject.neighbors(0,0,25)
        puts points.inspect
        expect(points.first[2]).to eq(3)
        expect(points.last[2]).to eq(4)
      end
    end
  end
end
