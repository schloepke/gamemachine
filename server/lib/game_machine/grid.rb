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
        JavaLib::Grid.reset_grids
      end

      def default_grid
        find_or_create('default')
      end

      def find_or_create(name)
        GameMachine.logger.info "CREATING GRID #{name}"
        grid_config = config.fetch(name)
        JavaLib::Grid.find_or_create(name,grid_config.fetch(:grid_size),
                                   grid_config.fetch(:cell_size))
      end
    end

  end
end
