module GameMachine
  module Navigation
    class DetourPath

      attr_reader :navmesh, :error
      def initialize(navmesh)
        @navmesh = navmesh
        @max_paths = 256
        @step_size = 1.0
        @error = nil

        unless navmesh.loaded?
          raise "Navmesh #{navmesh.id} not loaded"
        end

        @query_ptr = Detour.getQuery(navmesh.id)
      end

      def destroy_query
        Detour.freeQuery(@query_ptr)
      end

      # Detour coords:
      # z = unity x, x = unity z
      def find_path( start_x, start_y, start_z, end_x, end_y, end_z,
                    max_paths=@max_paths, step_size=@step_size)

        @error = nil

        ptr = Detour.getPathPtr(max_paths)
        paths_found = Detour.findPath(
          @query_ptr,start_z,start_y,start_x,
          end_z,end_y,end_x, max_paths, step_size, 0, ptr
        )

        if paths_found <= 0
          @error = paths_found
          return []
        end

        fptr = ptr.read_pointer()
        path = fptr.null? ? [] : ptr.get_array_of_float(0,paths_found*3)
        Detour.freePath(ptr)
        path.each_slice(3).map {|i| Vector.new(i[2],i[1],i[0])}.to_a
      end
    end
  end
end
