require 'ffi'
module Mono
  extend FFI::Library
  sofile = File.join(File.dirname(__FILE__), '../../mono/libactor.so')
  if File.exists?(sofile)
    ffi_lib sofile
    attach_function :load_mono, [:string], :void
    attach_function :load_assembly, [:string], :pointer
    attach_function :create_object, [:pointer, :string, :string], :pointer
    #attach_function :create_object, [:pointer, :string, :string], :uint32
    attach_function :destroy_object, [:uint32], :void
    attach_function :on_receive, [:pointer,:string,:int], :int
    attach_function :on_receive2, [:pointer, :string, :string, :string,:pointer,:int], :int
    #attach_function :on_receive, [:uint32,:string,:int], :int
    attach_function :unload_mono, [], :void
    attach_function :attach_current_thread, [], :void
  end
end

module  GameMachine
  class MonoUtil
    def self.load_assembly(path)
      Mono.load_assembly(path)
    end
  end
end
