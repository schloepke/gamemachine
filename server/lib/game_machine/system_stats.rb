module GameMachine
  class SystemStats < Actor::Base

    def post_init(*args)
      if getContext.system.name == 'cluster'
        @cluster = JavaLib::Cluster.get(getContext.system)
      end
      schedule_message('update',60,:seconds)
    end

    def on_receive(message)
      if message.is_a?(String) and message == 'update'

        JavaLib::Grid.grids.each do |name,grid|
          object_index_size = grid.objectIndex.length
          cellscache_size = grid.cellsCache.length
          cells_size = grid.cells.length
          GameMachine.logger.info "Grid #{name} ondex_size: #{object_index_size} ccache_size: #{cellscache_size} csize: #{cells_size}"
        end
      end
      
    end
  end
end
