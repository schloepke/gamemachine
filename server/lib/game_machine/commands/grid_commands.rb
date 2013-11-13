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

      def neighbors(x,z,type='player')
        grid.neighbors(x,z,type)
      end

      def track(id,x,z,y,entity_type='npc')
        grid.set(id,x,z,y,entity_type)
      end

    end
  end
end
