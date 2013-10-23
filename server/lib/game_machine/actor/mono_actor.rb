require 'base64'
module GameMachine
  module Actor
    class MonoActor < Base

      def post_init(*args)
        @path = args[0]
        @namespace = args[1]
        @klass = args[2]
        @domain = args[3]
      end

      def create_mono_object
        @thread_id = JRuby.reference(Thread.current).native_thread.id
        if @path.nil? or @namespace.nil? or @klass.nil?
          raise "Missing post_init args"
        end
        Mono.attach_current_thread(@domain)
        @image = Mono.load_assembly(@domain,@path)
        #@mono_object = Mono.create_object(image,namespace,klass)
        #if @mono_object == 0
        #  raise "Mono.create_object failed"
        #end
      end

      def postStop
        puts 'postStop'
        #Mono.destroy_object(@mono_object)
      end

      def on_receive(message)
        Mono.attach_current_thread(@domain)
        if @image.nil?
          create_mono_object
        end
        current_thread_id = JRuby.reference(Thread.current).native_thread.id
        if @thread_id != current_thread_id
          #raise "Invalid thread id #{current_thread_id} != #{@thread_id}"
        end
        Mono.attach_current_thread(@domain)
        bytes = message.to_byte_array
        byte_string = bytes.to_s
        encoded_bytes = Base64.encode64(byte_string)
        encoded_bytes_size = encoded_bytes.size
        #mem_buf = FFI::MemoryPointer.new(:uchar, message.size)
        #mem_buf.put_bytes(0, message.to_s)
        #res = Mono.on_receive(@mono_object,mem_buf,message.size)
        actor_id = "#{self.class.name}-#{current_thread_id}"
        #puts "ENCODED #{encoded_bytes} = #{encoded_bytes_size}"
        res = Mono.on_receive2(@domain,@image,@namespace,@klass,actor_id, encoded_bytes, encoded_bytes_size)
        #res = Mono.on_receive(@mono_object,bytes.to_s,bytes.size)
        if res == 0
          raise "Mono managed code threw exception, restarting actor"
        end

        sender.tell(message,self)
      end

    end
  end
end
