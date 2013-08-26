require 'spec_helper'
require 'matrix'
module GameMachine
  describe JavaLib::Grid do

    
    subject do
      JavaLib::Grid.new(100,25)
    end

    let(:transform) do
      transform = Transform.new
      transform.set_vector3(
        Vector3.new.set_x(0).set_y(0).set_z(0)
      )
      transform
    end

    let(:entity) do
      Entity.new.set_id('0').set_transform(transform)
    end

    describe "#set" do
      it "returns true when value is set" do
        expect(subject.set('blah',0,0,0,'player')).to be_true
      end

      it "creates a grid point in correct cell" do
        subject.set('blah',0,0,0,'player')
        point = subject.grid_values_in_cell(0).first
        expect(point.x).to eq(0)
        expect(point.y).to eq(0)
      end
    end

    describe "#get" do
      it "Returns the values for the given id" do
        subject.set('blah',0,0,3,'player')
        grid_value = subject.get('blah')

        expect(grid_value.id).to eq('blah')
        expect(grid_value.cell).to eq(0)
        expect(grid_value.x).to eq(0)
        expect(grid_value.y).to eq(0)
        expect(grid_value.z).to eq(3)
      end
    end

    describe "#remove" do
      it "Removes the cell entry" do
        subject.set('blah',0,0,3,'player')
        subject.remove('blah')
        points = subject.grid_values_in_cell(0)
        expect(points).to be_empty
      end
    end

    describe "#cells_within_radius" do
      it "returns the correct cells" do
        subject.cellsWithinRadius(0,0,50)
      end
    end

    describe "#neighbors" do

      it "returns empty array when no neighbors found" do
        expect(subject.neighbors(0,0,25,nil,nil)).to be_empty
      end

      it "returns all objects within given radius of cell" do
        subject.set('blah',0,0,0,'player')
        points = subject.neighbors(0,0,25,nil,nil)
        expect(points.first.cell).to eq(0)
      end

      it "returns entities of the correct type" do
        subject.set('blah',0,0,0,'player')
        points = subject.neighbors(0,0,50,'player',nil)
        expect(points.first.cell).to eq(0)

      end

      it "does not return cells out of range" do
        subject.set('blah',0,0,0,'player')
        expect(subject.neighbors(50,50,25,nil,nil)).to be_empty
      end

      it "returns multiple points in the same cell" do
        subject.set('blah',3,3,0,'player')
        subject.set('blah2',4,4,0,'player')
        points = subject.neighbors(0,0,25,nil,nil).to_a
        expect(points.last.x).to eq(3)
        expect(points.first.x).to eq(4)
      end

      context "caller id is passed" do

      end
    end
  end
end
