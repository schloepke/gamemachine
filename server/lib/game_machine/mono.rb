require 'ffi'
module Mono
  extend FFI::Library
  sofile = File.join(File.dirname(__FILE__), '../../mono/libactor.so')
  if File.exists?(sofile)
    ffi_lib sofile
    attach_function :load_mono, [:string], :void
    attach_function :load_assembly, [:pointer, :string], :pointer
    attach_function :create_object, [:pointer, :string, :string], :pointer
    #attach_function :create_object, [:pointer, :string, :string], :uint32
    attach_function :destroy_object, [:uint32], :void
    attach_function :on_receive, [:pointer,:string,:int], :int
    attach_function :on_receive2, [:pointer, :pointer, :pointer, :pointer, :pointer,:pointer,:int], :int
    attach_function :ftest, [:string, :string, :string,:string,:int], :int
    #attach_function :on_receive, [:uint32,:string,:int], :int
    attach_function :unload_mono, [], :void
    attach_function :create_domain, [:string], :pointer
    attach_function :attach_current_thread, [:pointer], :void
    
    attach_function :set_callback, [:int,:pointer], :void

    Callback = FFI::Function.new(:void, [:string,:int, :pointer]) do |message,len,mem_buf|
      #puts "CALLBACK #{mem_buf}"
      mem_buf.put_string(0, "test 1 2 3")
    end
  end
end

