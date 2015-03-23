module GameMachine
  module Commands
    class GridCommands

      attr_reader
      def initialize
        @aoe_grid = JavaLib::GameGrid.get_game_grid(Application.config.default_game_id,"aoe",0)
        @grid = JavaLib::GameGrid.get_game_grid(Application.config.default_game_id,"default",0)
      end

      def grid
        JavaLib::GameGrid.get_game_grid(Application.config.default_game_id,"default")
      end

      def aoe_grid
        JavaLib::GameGrid.get_game_grid(Application.config.default_game_id,"aoe")
      end

      def get_neighbors_for(id,entity_type='player')
        return [] if grid.nil?
        grid.getNeighborsFor(id,entity_type)
      end

      def neighbors(x,y,type=MessageLib::TrackData::EntityType::PLAYER)
        return [] if grid.nil?
        grid.neighbors(x,y,type)
      end

      def remove(id)
        return if grid.nil?
        grid.remove(id)
        aoe_grid.remove(id)
      end

      def track(id,x,y,z,entity_type=MessageLib::TrackData::EntityType::NPC)
        return if grid.nil?
        grid.set(id,x.to_f,y.to_f,z,entity_type)
        aoe_grid.set(id,x.to_f,y.to_f,z,entity_type)
      end

    end
  end
end
