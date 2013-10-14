require 'ffi'
module Mono
  extend FFI::Library
  sofile = File.join(File.dirname(__FILE__), '../../mono/libactor.so')
  if File.exists?(sofile)
    ffi_lib sofile
    attach_function :load_mono, [:string], :void
    attach_function :load_assembly, [:string], :pointer
    attach_function :create_object, [:pointer], :uint32
    attach_function :destroy_object, [:uint32], :void
    attach_function :on_receive, [:uint32,:pointer,:int], :void
    attach_function :unload_mono, [], :void
    attach_function :attach_current_thread, [], :void
  end
end

module  GameMachine

  class MonoLib
    def self.init_mono
      Mono.load_mono("/home2/chris/game_machine/server/mono/actor.dll")
      @image = Mono.load_assembly("/home2/chris/game_machine/server/mono/actor.dll")
    end

    def self.image
      @image
    end

    def self.obj
      @obj
    end
  end


  class MonoTest < Actor::Base

    def post_init(*args)
      Mono.attach_current_thread
      @obj = Mono.create_object(MonoLib.image)
    end

    def postStop
      puts "POST STOP CALLED"
      Mono.destroy_object(@obj)
    end

    def on_receive(message)
      #puts "TEST RECEIVED #{message}"
      Mono.attach_current_thread
      message = message.to_byte_array
      mem_buf = FFI::MemoryPointer.new(:uchar, message.size)
      mem_buf.put_bytes(0, message.to_s)
      Mono.on_receive(@obj,mem_buf,message.size)
    end
  end
end
