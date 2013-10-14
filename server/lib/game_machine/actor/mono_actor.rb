module GameMachine
  module Actor
    class MonoActor < Base

      def post_init(*args)
        image = args[0]
        namespace = args[1]
        klass = args[2]
        if image.nil? or namespace.nil? or klass.nil?
          raise "Missing post_init args"
        end
        Mono.attach_current_thread
        @mono_object = Mono.create_object(image,namespace,klass)
        if @mono_object == 0
          raise "Mono.create_object failed"
        end
      end

      def postStop
        Mono.destroy_object(@mono_object)
      end

      def on_receive(message)
        Mono.attach_current_thread
        message = message.to_byte_array
        mem_buf = FFI::MemoryPointer.new(:uchar, message.size)
        mem_buf.put_bytes(0, message.to_s)
        res = Mono.on_receive(@mono_object,mem_buf,message.size)
        if res == 0
          raise "Mono managed code threw exception, restarting actor"
        end
      end

    end
  end
end
