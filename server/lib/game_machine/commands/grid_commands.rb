module GameMachine
  module Commands
    class GridCommands

      def find_by_id(id)
        GameMachine::GameSystems::EntityTracking::GRID.get(id)
      end

      def neighbors(x,z,type='player')
        GameMachine::GameSystems::EntityTracking.neighbors_from_grid(x,z,type)
      end

      def track(id,x,z,y,entity_type='npc')
        GameMachine::GameSystems::EntityTracking::GRID.set(id,x,z,y,entity_type)
      end

    end
  end
end
