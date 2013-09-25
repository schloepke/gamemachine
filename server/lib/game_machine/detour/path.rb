require 'ffi'

module GameMachine
  module Detour
    extend FFI::Library
    sofile = File.join(File.dirname(__FILE__), '../../detour/libpath.so')
    if File.exists?(sofile)
      ffi_lib sofile
    end

    class Path

    end

  end
end
