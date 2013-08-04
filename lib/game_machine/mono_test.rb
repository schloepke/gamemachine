require 'ffi'
module Mono
  extend FFI::Library
  ffi_lib '/home/chris/game_machine/mono/libactor.so'
  attach_function :load_mono, [:string], :void
  attach_function :test_mono, [], :int
  attach_function :load_assembly, [:string], :pointer
  attach_function :create_object, [:pointer], :pointer
  attach_function :on_receive, [:pointer,:pointer,:int], :void
  attach_function :unload_mono, [], :void
end

module  GameMachine

  class MonoLib
    def self.test_mono
      Mono.test_mono
    end
    def self.init_mono
      Mono.load_mono("/home/chris/game_machine/mono/actor.dll")
      #image = Mono.load_assembly("/home/chris/game_machine/mono/actor.dll")
      #@obj = Mono.create_object(image)
      #Mono.unload_mono
    end
  end

  class MonoTest < Actor::Base



    def self.thread_test
      threads = []
      10.times do
        threads << Thread.new do
          10000.times do
            data = "adsfasdf\0\x097asdf757sfasdfsdf\n\n\tadfadsf"
            mem_buf = FFI::MemoryPointer.new(:char, data.size)
            mem_buf.put_bytes(0, data)
            Mono.on_receive(obj,mem_buf,data.size)
          end
        end
      end
      threads.map(&:join)
    end

    def post_init(*args)

    end

    def on_receive(message)
      puts 'MONO'
      mem_buf = FFI::MemoryPointer.new(:char, message.size)
      mem_buf.put_bytes(0, message)
      Mono.on_receive(self.obj,mem_buf,message.size)
    end
  end
end
