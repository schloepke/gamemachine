module GameMachine
  class SystemStats < Actor::Base

    def post_init(*args)
      @last_count = 0
      if getContext.system.name == 'cluster'
        @cluster = JavaLib::Cluster.get(getContext.system)
      end
      schedule_message('message_count',1,:seconds)
      schedule_message('update',60,:seconds)
    end

    def on_receive(message)
      if message.is_a?(String)
        if message == 'message_count'
          current_count = JavaLib::MessageGateway.messageCount.incrementAndGet
          diff = current_count - @last_count
          GameMachine.logger.info "MessagesPerSecond: #{diff}"
          @last_count = current_count
        elsif message == 'update'
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
end
