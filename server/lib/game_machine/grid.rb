module GameMachine
  class Grid

    class << self

      def reset_grids
        @grids = java.util.concurrent.ConcurrentHashMap.new
      end

      def grids
        if @grids
          return @grids
        else
          reset_grids
        end
      end

      def default_grid
        find_or_create('default')
      end

      def find_or_create(
        name,
        grid_size=Application.config.world_grid_size,
        cell_size=Application.config.world_grid_cell_size
      )
        unless grids.containsKey(name)
          grids.put(name,JavaLib::Grid.new(grid_size,cell_size))
        end

        grids.get(name)
      end
    end

  end
end
