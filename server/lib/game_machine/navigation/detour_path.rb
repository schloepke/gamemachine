module GameMachine
  module Navigation
    class DetourPath

      attr_reader :navmesh, :error
      def initialize(navmesh)
        @navmesh = navmesh
        @path_max = 256*3
        @error = nil

        unless navmesh.loaded?
          raise "Navmesh #{navmesh.id} not loaded"
        end

        @query_ptr = Detour.getQuery(navmesh.id)
      end

      def destroy_query
        Detour.freeQuery(@query_ptr)
      end

      def find_path(start_x,start_y,start_z,end_x,end_y,end_z)
        @error = nil

        #ptr = FFI::MemoryPointer.new(:pointer,@path_max)
        ptr = Detour.getPathPtr()
        paths_found = Detour.findPath(
          @query_ptr,start_x,start_y,start_z,end_x,end_y,end_z,ptr
        )

        if paths_found <= 0
          @error = paths_found
          return []
        end

        fptr = ptr.read_pointer()
        path = fptr.null? ? [] : ptr.get_array_of_float(0,paths_found*3)
        Detour.freePath(ptr)
        path.each_slice(3).map {|i| Vector.new(i[0],i[1],i[2])}.to_a
      end
    end
  end
end
