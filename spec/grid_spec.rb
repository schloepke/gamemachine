require 'spec_helper'
require 'matrix'
module GameMachine
  describe Physics::Grid do

    subject do
      Physics::Grid.new(100,25)
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
      it "creates a grid point in correct cell" do
        entity.transform.vector3.set_x(0).set_y(0)
        subject.set(entity)
        point = subject.points_in_cell(0).first[2]
        expect(point.transform.vector3.x).to eq(0)
        expect(point.transform.vector3.y).to eq(0)
      end
    end

    describe "#get" do
      it "Returns the values for the given id" do
        entity.transform.vector3.set_x(0).set_y(0).set_z(3)
        subject.set(entity)
        id,cell,point = subject.get('0')

        expect(id).to eq('0')
        expect(cell).to eq(0)
        expect(point.transform.vector3.x).to eq(0)
        expect(point.transform.vector3.y).to eq(0)
        expect(point.transform.vector3.z).to eq(3)
      end
    end

    describe "#remove" do
      it "Removes the cell entry" do
        subject.set(entity)
        subject.remove('0')
        points = subject.points_in_cell(0)
        expect(points).to eq([])
      end
    end

    describe "#neighbors" do
      it "returns empty array when no neighbors found" do
        expect(subject.neighbors(0,0,25)).to eq([])
      end

      it "returns all objects within given radius of cell" do
        subject.set(entity)
        points = subject.neighbors(0,0,25)
        expect(points.first[1]).to eq(0)
      end

      it "does not return cells out of range" do
        subject.set(entity)
        expect(subject.neighbors(50,50,25)).to eq([])
      end

      it "returns multiple points in the same cell" do
        t1 = entity.clone.set_id('0')
        t2 = entity.clone.set_id('1')
        t1.transform.vector3.set_x(3).set_y(0)
        t2.transform.vector3.set_x(4).set_y(0)
        subject.set(t1)
        subject.set(t2)
        points = subject.neighbors(0,0,25)
        expect(points.first[2].transform.vector3.x).to eq(3)
        expect(points.last[2].transform.vector3.x).to eq(4)
      end
    end
  end
end
