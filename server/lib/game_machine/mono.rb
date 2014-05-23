require 'ffi'
require 'base64'
module Mono
  extend FFI::Library
  sofile = File.join(File.dirname(__FILE__), '../../mono/libactor.so')
  if File.exists?(sofile)
    ffi_lib sofile
    attach_function :load_mono, [:string], :void
    attach_function :load_assembly, [:pointer, :string], :pointer
    attach_function :create_object, [:pointer, :string, :string], :pointer
    attach_function :destroy_object, [:uint32], :void
    attach_function :on_receive2, [:pointer, :pointer, :pointer, :pointer, :pointer,:pointer,:int], :int
    attach_function :ftest, [:string, :string, :string,:string,:int], :int
    attach_function :unload_mono, [], :void
    attach_function :create_domain, [:string], :pointer
    attach_function :attach_current_thread, [:pointer], :void
    attach_function :set_callback, [:int,:pointer], :void


    Callback = FFI::Function.new(:void, [:string,:int, :pointer]) do |message,len,mem_buf|
      message = Base64.decode64(message)
      entity = GameMachine::MessageLib::Entity.parse_from(message.to_java_bytes)
      #puts entity.id
      thread_id = JRuby.reference(Thread.current).native_thread.id
      Vm.instance.response = entity
      #puts "CALLBACK #{len} #{message} #{mem_buf}"
      #mem_buf.put_string(0, "test 1 2 3")
    end
  end

  class TaskRunner
    include Callable

    def self.executor
      core_pool_size = 1
      maximum_pool_size = 1
      keep_alive_time = 30000 # keep idle threads 5 minutes around
      ThreadPoolExecutor.new(core_pool_size, maximum_pool_size, keep_alive_time, TimeUnit::SECONDS, LinkedBlockingQueue.new)
    end

    def initialize(namespace,klass,message)
      @namespace = namespace
      @klass = klass
      @message = message
    end

    def call
      Vm.instance.call_mono(@namespace,@klass,@message)
    end
  end

  class Vm
    include Singleton
    attr_reader :domain, :image, :executor
    attr_accessor :response

    def load
      config = File.join(GameMachine.app_root,'mono/app.config')
      dll_path = File.join(GameMachine.app_root,'mono',GameMachine::Application.config.mono_dll)
      Mono.load_mono(dll_path)
      @domain = Mono.create_domain(config)
      Mono.attach_current_thread(@domain)
      @image = Mono.load_assembly(@domain,dll_path)
      Mono.set_callback(1,Mono::Callback)
      @executor = TaskRunner.executor
    end

    def unload
      @executor.shutdown if @executor
    end

    def send_message(namespace,klass,message)
      task = FutureTask.new(TaskRunner.new(namespace,klass,message))
      @executor.execute(task)
      task.get
    end

    def call_mono(namespace,klass,message)
      @response = nil
      Mono.attach_current_thread(domain)
      thread_id = JRuby.reference(Thread.current).native_thread.id
      bytes = message.to_byte_array
      byte_string = bytes.to_s
      encoded_bytes = Base64.encode64(byte_string)
      encoded_bytes_size = encoded_bytes.size
      actor_id = thread_id.to_s
      if actor_id == '' or actor_id.nil?
        puts "actor_id invalid #{actor_id}"
        return
      end

      ns_mem_buf = FFI::MemoryPointer.new(:string, namespace.size)
      ns_mem_buf.put_string(0, namespace)
      klass_mem_buf = FFI::MemoryPointer.new(:string, klass.size)
      klass_mem_buf.put_string(0, klass)
      bytes_mem_buf = FFI::MemoryPointer.new(:string, encoded_bytes_size)
      bytes_mem_buf.put_string(0, encoded_bytes)
      mem_buf = FFI::MemoryPointer.new(:string, actor_id.size)
      mem_buf.put_string(0, actor_id)

      return_val = Mono.on_receive2(domain,image,ns_mem_buf,klass_mem_buf,mem_buf, bytes_mem_buf, encoded_bytes_size)
      if return_val == 0
        raise "Mono managed code threw exception, restarting actor"
      end
      response
    end

  end
end

