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
        expect(subject.set(entity,nil)).to be_true
      end

      it "creates a grid point in correct cell" do
        entity.transform.vector3.set_x(0).set_y(0)
        subject.set(entity,nil)
        point = subject.grid_values_in_cell(0).first.entity
        expect(point.transform.vector3.x).to eq(0)
        expect(point.transform.vector3.y).to eq(0)
      end
    end

    describe "#get" do
      it "Returns the values for the given id" do
        entity.transform.vector3.set_x(0).set_y(0).set_z(3)
        subject.set(entity,nil)
        grid_value = subject.get('0')

        expect(grid_value.id).to eq('0')
        expect(grid_value.cell).to eq(0)
        expect(grid_value.entity.transform.vector3.x).to eq(0)
        expect(grid_value.entity.transform.vector3.y).to eq(0)
        expect(grid_value.entity.transform.vector3.z).to eq(3)
      end
    end

    describe "#remove" do
      it "Removes the cell entry" do
        subject.set(entity,nil)
        subject.remove('0')
        points = subject.grid_values_in_cell(0)
        expect(points).to be_empty
      end
    end

    describe "#neighbors" do

      it "returns empty array when no neighbors found" do
        expect(subject.neighbors(0,0,25,nil)).to be_empty
      end

      it "returns all objects within given radius of cell" do
        subject.set(entity,nil)
        points = subject.neighbors(0,0,25,nil)
        expect(points.first.cell).to eq(0)
      end

      it "returns entities of the correct type" do
        subject.set(entity,'player')
        points = subject.neighbors(0,0,50,'player')
        expect(points.first.cell).to eq(0)

      end

      it "does not return cells out of range" do
        subject.set(entity,nil)
        expect(subject.neighbors(50,50,25,nil)).to be_empty
      end

      it "returns multiple points in the same cell" do
        t1 = entity.clone.set_id('0')
        t2 = entity.clone.set_id('1')
        t1.transform.vector3.set_x(3).set_y(0)
        t2.transform.vector3.set_x(4).set_y(0)
        subject.set(t1,nil)
        subject.set(t2,nil)
        points = subject.neighbors(0,0,25,nil).to_a
        expect(points.last.entity.transform.vector3.x).to eq(3)
        expect(points.first.entity.transform.vector3.x).to eq(4)
      end

      context "caller id is passed" do
        it "does not return unchanged values" do
          subject.set(entity,nil)
          points = subject.neighbors(0,0,25,nil,'me')
          expect(points.first.cell).to eq(0)

          points = subject.neighbors(0,0,25,nil,'me')
          expect(points).to be_empty
        end

        it "does return value if changed since last search" do
          subject.set(entity,nil)
          points = subject.neighbors(0,0,25,nil,'me')
          expect(points.first.cell).to eq(0)

          update_entity = Entity.new.set_id('0').
            set_transform(
              Transform.new.set_vector3(Vector3.new.set_x(1).set_y(1).set_z(0))
          )
          subject.set(update_entity,nil)
          points = subject.neighbors(0,0,25,nil,'me')
          expect(points.first.cell).to eq(0)
        end
      end
    end
  end
end
