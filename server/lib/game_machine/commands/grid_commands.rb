module GameMachine
  module Commands
    class GridCommands

      attr_reader :grid_name, :aoe_grid, :grid
      def initialize
        @grid_name = 'default'
        @aoe_grid = Grid.find_or_create('aoe',4000,5)
        @grid = Grid.find_or_create(grid_name)
      end

      def set_grid(grid_name)
        @grid_name = grid_name
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
