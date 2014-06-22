module GameMachine
  module Commands
    class GridCommands

      attr_reader :aoe_grid, :grid
      def initialize
        @aoe_grid = Grid.find_or_create('aoe')
        @grid = Grid.find_or_create('default')
      end

      def find_by_id(id)
        grid.get(id)
      end

      def get_neighbors_for(id,entity_type='player')
        grid.getNeighborsFor(id,entity_type)
      end

      def neighbors(x,y,type='player')
        grid.neighbors(x,y,type)
      end

      def remove(id)
        grid.remove(id)
        aoe_grid.remove(id)
      end

      def track(id,x,y,z,entity_type='npc')
        grid.set(id,x,y,z,entity_type)
        aoe_grid.set(id,x,y,z,entity_type)
      end

    end
  end
end
