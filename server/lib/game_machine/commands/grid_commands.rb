module GameMachine
  module Commands
    class GridCommands

      attr_reader :grid_name
      def initialize
        @grid_name = 'default'
      end

      def set_grid(grid_name)
        @grid_name = grid_name
      end

      def grid
        Grid.find_or_create(grid_name)
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

      def track(id,x,y,z,entity_type='npc')
        grid.set(id,x,y,z,entity_type)
      end

    end
  end
end
