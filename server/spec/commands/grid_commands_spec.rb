require 'spec_helper'

module GameMachine
  module Commands
    describe GridCommands do

      let(:grid) do
        GameMachine::GameSystems::EntityTracking::GRID
      end

      subject{GridCommands.new}

      describe "#find_by_id" do
        it "retrieves a grid value from the grid by id" do
          expect(grid).to receive(:get).with('1')
          subject.find_by_id('1')
        end
      end

      describe "#track" do
        it "sends entity id and coordinates to the grid" do
          expect(grid).to receive(:set).with('1',1.1,1.2,1.3,'player')
          subject.track('1',1.1,1.2,1.3,'player')
        end
      end

      describe "#neighbors" do
        it "gets a list of neighbors from the grid with default entity type" do
          expect(GameSystems::EntityTracking).to receive(:neighbors_from_grid).with(1.1,1.2,'player')
          subject.neighbors(1.1,1.2)
        end

        it "gets a list of neighbors from the grid with explicit entity type" do
          expect(GameSystems::EntityTracking).to receive(:neighbors_from_grid).with(1.1,1.2,'npc')
          subject.neighbors(1.1,1.2,'npc')
        end
      end

    end
  end
end
