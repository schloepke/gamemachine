require 'ffi'
require 'benchmark'
module GameMachine
  module Navigation
    module Detour
      extend FFI::Library
      sofile = File.join(File.dirname(__FILE__), '../../../detour/libpathfind.so')
      if File.exists?(sofile)
        ffi_lib sofile
        attach_function :findPath, [:int,:float,:float,:float,:float,:float,:float, :pointer], :int
        attach_function :loadNavMesh, [:int, :string], :int
      end

      class Path
        def self.find_path
          load_res = Detour.loadNavMesh(1,"/home2/chris/game_machine/server/detour/test2.bin")
          unless load_res == 1
            puts "navmesh failed to load"
            return
          end
          time = Benchmark.realtime do
            10.times do
              ptr = FFI::MemoryPointer.new(:pointer,256*3)
              len = Detour.findPath(1,10.0,0,10.0,109.0,0,109.0,ptr)
              if len <= 0
                puts "error #{len}"
                return
              end
              fptr = ptr.read_pointer()
              path = fptr.null? ? [] : ptr.get_array_of_float(0,len*3)
              #puts path.inspect
            end
          end
          puts "query time #{time}"
        end
      end
    end
  end
end
