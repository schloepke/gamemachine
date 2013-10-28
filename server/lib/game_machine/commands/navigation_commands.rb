module GameMachine
  module Commands
    class NavigationCommands

      def load_navmesh(id,path)
        if File.exists?(path)
          navmesh = GameMachine::Navigation::DetourNavmesh.create(id,path)
          navmesh.load_mesh!
          return navmesh
        else
          raise "Meshfile #{path} does not exist"
        end
      end

      def navmesh(id)
        GameMachine::Navigation::DetourNavmesh.find(1)
      end

      def query_ref(navmesh)
        GameMachine::Navigation::DetourPath.new(navmesh)
      end
    end
  end
end
