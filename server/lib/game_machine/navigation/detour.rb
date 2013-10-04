require 'ffi'
require 'benchmark'
module GameMachine
  module Navigation
    module Detour
      extend FFI::Library
      sofile = File.join(File.dirname(__FILE__), '../../../detour/libpathfind.so')
      if File.exists?(sofile)
        ffi_lib sofile
        attach_function :findPath, [:pointer,:float,:float,:float,:float,:float,:float, :int, :pointer], :int
        attach_function :loadNavMesh, [:int, :string], :int
        attach_function :freePath, [:pointer], :void
        attach_function :freeQuery, [:pointer], :void
        attach_function :getQuery, [:int], :pointer
        attach_function :getPathPtr, [:int], :pointer
      end

    end
  end
end
