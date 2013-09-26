module GameMachine
  module Navigation
    class DetourNavmesh

      def self.meshes
        if @meshes
          @meshes
        else
          @meshes = java.util.concurrent.ConcurrentHashMap.new
        end
      end

      def self.create(id,meshfile)
        if meshes.has_key?(id)
          return false
        end
        meshes[id] = new(id,meshfile)
      end

      def self.find(id)
        meshes.fetch(id,nil)
      end

      attr_reader :id, :meshfile, :loaded
      def initialize(id,meshfile)
        @id = id
        @meshfile = meshfile
        @loaded = false
      end

      def loaded?
        loaded
      end

      def load_mesh!
        if loaded?
          raise "DetourNavmesh #{id} already loaded"
        end

        load_res = Detour.loadNavMesh(id,meshfile)
        unless load_res == 1
          raise "DetourNavmesh #{id} failed to load (#{load_res})"
        end
        @loaded = true
        load_res
      end

    end
  end
end
