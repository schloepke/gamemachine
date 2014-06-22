module GameMachine
  class Grid

    class << self

      def config
        if @config
          return @config
        else
          @config = java.util.concurrent.ConcurrentHashMap.new
        end
      end

      def update_frequency_for(name)
        config.fetch(name)[:update_frequency]
      end

      def load_from_config
        Application.config.grids.each do |name,value|
          grid_size,cell_size,update_frequency = value.split(',')
          config[name] = {
            :grid_size => grid_size.to_i,
            :cell_size => cell_size.to_i,
            :update_frequency => update_frequency.to_i
          }
          find_or_create(name)
        end
      end

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

      def find_or_create(name)
        unless grids.containsKey(name)
          GameMachine.logger.info "CREATING GRID #{name}"
          grid_config = config.fetch(name)
          grid = JavaLib::Grid.new(grid_config.fetch(:grid_size),
                                   grid_config.fetch(:cell_size))
          grids.put(name,grid)
        end

        grids.fetch(name)
      end
    end

  end
end
