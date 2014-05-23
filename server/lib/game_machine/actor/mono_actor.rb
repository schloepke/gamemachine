require 'base64'
module GameMachine
  module Actor
    class MonoActor < Base

      def self.images
        @@images ||= java.util.concurrent.ConcurrentHashMap.new
      end

      def self.call_mono(message,image,domain,namespace,klass)
        thread_id = JRuby.reference(Thread.current).native_thread.id
        #Mono.attach_current_thread(domain)
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

        #puts "ENCODED #{encoded_bytes} = #{encoded_bytes_size}"
        #res = Mono.ftest(@namespace,@klass,actor_id, encoded_bytes, encoded_bytes_size)
        #puts 'before_on_receive'
        res = Mono.on_receive2(domain,image,ns_mem_buf,klass_mem_buf,mem_buf, bytes_mem_buf, encoded_bytes_size)
        #puts 'after_on_receive'
        #res = Mono.on_receive(@mono_object,bytes.to_s,bytes.size)
        if res == 0
          raise "Mono managed code threw exception, restarting actor"
        end
      end

      def post_init(*args)
        @path = args[0]
        @namespace = args[1]
        @klass = args[2]
        @domain = args[3]
        @image = args[4]
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

      def ensure_image
        thread_id = JRuby.reference(Thread.current).native_thread.id
        image = self.class.images.fetch(thread_id,nil)
        if image.nil?
          Mono.attach_current_thread(@domain)
          image = Mono.load_assembly(@domain,@path)
          self.class.images[thread_id] = image
        end
        image
      end

      def postStop
        puts 'postStop'
        #Mono.destroy_object(@mono_object)
      end

      def on_receive(message)
        Mono.attach_current_thread(@domain)

        self.class.call_mono(message,@image,@domain,@namespace,@klass)
        sender.tell(message,self)
      end

    end
  end
end
