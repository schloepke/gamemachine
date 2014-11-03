module GameMachine
  module Commands
    class GridCommands

      attr_reader
      def initialize
        @aoe_grid = JavaLib::GameGrid.get_game_grid(Application.config.default_game_id,"aoe")
        @grid = JavaLib::GameGrid.get_game_grid(Application.config.default_game_id,"default")
      end

      def grid
        @grid ||= JavaLib::GameGrid.get_game_grid(Application.config.default_game_id,"default")
      end

      def aoe_grid
        @aoe_grid ||= JavaLib::GameGrid.get_game_grid(Application.config.default_game_id,"aoe")
      end

      def get_neighbors_for(id,entity_type='player')
        return [] if grid.nil?
        grid.getNeighborsFor(id,entity_type)
      end

      def neighbors(x,y,type='player')
        return [] if grid.nil?
        grid.neighbors(x,y,type)
      end

      def remove(id)
        return if grid.nil?
        grid.remove(id)
        aoe_grid.remove(id)
      end

      def track(id,x,y,z,entity_type='npc')
        return if grid.nil?
        grid.set(id,x,y,z,entity_type)
        aoe_grid.set(id,x,y,z,entity_type)
      end

    end
  end
end
