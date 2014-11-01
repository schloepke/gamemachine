require 'spec_helper_minimal'
module GameMachine
  describe JavaLib::Grid do

    
    let(:player_type) {MessageLib::TrackData::EntityType::PLAYER}
    subject do
      JavaLib::Grid.new(2000,25)
    end


    describe "#set" do
      it "returns true when value is set" do
        expect(subject.set('blah',0,0,0,player_type)).to be_truthy
      end

      it "creates a grid point in correct cell" do
        subject.set('blah',0,0,0,player_type)
        point = subject.grid_values_in_cell(0).first
        expect(point.x).to eq(0)
        expect(point.y).to eq(0)
      end
    end

    describe "#get" do
      it "Returns the values for the given id" do
        subject.set('blah',0,0,3,player_type)
        grid_value = subject.get('blah')

        expect(grid_value.id).to eq('blah')
        expect(grid_value.x).to eq(0)
        expect(grid_value.y).to eq(0)
        expect(grid_value.z).to eq(3)
      end
    end

    describe "#remove" do
      it "Removes the cell entry" do
        subject.set('blah',0,0,3,player_type)
        subject.remove('blah')
        points = subject.grid_values_in_cell(0)
        expect(points).to be_empty
        expect(subject.neighbors(0,0,25,nil)).to be_empty
      end
    end

    describe "#cells_within_radius" do
      it "returns the correct cells" do
        subject.cellsWithinRadius(0,0,50)
      end
    end

    describe "#neighbors" do

      it "returns empty array when no neighbors found" do
        expect(subject.neighbors(0,0,nil)).to be_empty
      end

      it "returns all objects within given radius of cell" do
        subject.set('blah',0,0,0,player_type)
        points = subject.neighbors(0,0,nil)
        tdata = points.first
        cell = subject.hash(tdata.x,tdata.y)
        expect(cell).to eq(0)
      end

      it "returns entities of the correct type" do
        subject.set('blah',0,0,0,player_type)
        points = subject.neighbors(0,0,player_type)
        tdata = points.first
        cell = subject.hash(tdata.x,tdata.y)
        expect(cell).to eq(0)
      end

      it "does not return cells out of range" do
        subject.set('blah',0,0,0,player_type)
        expect(subject.neighbors(50,50,nil)).to be_empty
      end

      it "returns multiple points in the same cell" do
        subject.set('blah',3,3,0,player_type)
        subject.set('blah2',4,4,0,player_type)
        points = subject.neighbors(0,0,nil).to_a
        expect(points.last.x).to eq(3)
        expect(points.first.x).to eq(4)
      end

      it "returns correct entities 2" do
        subject.set('blah',40,40,0,player_type)
        subject.set('blah2',4,4,0,player_type)
        points = subject.neighbors(78,78,nil).to_a
        v1 = Vector.new(40,40)
        v2 = Vector.new(78,78)
        expect(points).to be_empty
      end

      xit "returns correct entities 3" do
        grid = JavaLib::Grid.new(2000,50)
        grid.set('blah',740,740,0,player_type)
        grid.set('blah2',880,880,0,player_type)
        points = grid.neighbors(800,800,nil).to_a
        v1 = Vector.new(740,740)
        v2 = Vector.new(880,880)
        v3 = Vector.new(800,800)
        #puts (v1.distance(v3))
        #puts (v2.distance(v3))
        expect(points).to be_empty
      end

      describe "#cells_within_radius" do

        xit "should return correct cells 1" do
          grid = JavaLib::Grid.new(2000,50)
          grid.set('blah',740,740,0,player_type)
          grid.set('blah2',880,880,0,player_type)
          points = grid.cells_within_radius(800,800).to_a
          v1 = Vector.new(740,740)
          v2 = Vector.new(880,880)
          v3 = Vector.new(800,800)
          #puts (v1.distance(v3))
          #puts (v2.distance(v3))
          expect(points).to be_empty
        end

        it "should return correct cells 1" do
          grid = JavaLib::Grid.new(200,25)
          entities = []
          v3 = Vector.new(98,98)
          entities << v1 = Vector.new(60,60)
          entities << v1 = Vector.new(140,140)
          entities.each do |entity|
            grid.set('blah',entity.x,entity.y,0,player_type)
            puts "Entity #{entity.x}.#{entity.y} distance=#{v3.distance(entity)}"
          end
          
          puts grid.cells_within_radius(v3.x,v3.y).to_a.inspect
          results = grid.neighbors(v3.x,v3.y,nil).to_a
          results.each do |point|
            puts "got #{point.x} #{point.y} #{Vector.new(point.x,point.y).distance(v3)}"
          end
          
          expect(results).to be_empty
        end
      end
    end
  end
end
