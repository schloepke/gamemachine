module GameMachine
  module Navigation
    class DetourPath

      attr_reader :navmesh, :error
      def initialize(navmesh)
        @navmesh = navmesh
        @path_max = 256*3
        @error = nil
      end

      def find_path(start_x,start_y,start_z,end_x,end_y,end_z)
        @error = nil
        unless navmesh.loaded?
          raise "Navmesh #{navmesh.id} not loaded"
        end

        ptr = FFI::MemoryPointer.new(:pointer,@path_max)
        paths_found = Detour.findPath(
          navmesh.id,start_x,start_y,start_z,end_x,end_y,end_z,ptr
        )

        if paths_found <= 0
          @error = paths_found
          return []
        end
        fptr = ptr.read_pointer()
        path = fptr.null? ? [] : ptr.get_array_of_float(0,paths_found*3)
        path = path.each_slice(3).to_a
        if path.size > 1
          #path.shift
        end
        path
      end
    end
  end
end
