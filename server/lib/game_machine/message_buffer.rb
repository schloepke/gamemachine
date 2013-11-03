module GameMachine
  class MessageBuffer

    attr_reader :bytes, :next_message_length
    def initialize
      reset
    end

    def messages
      byte_messages = []
      return byte_messages if bytes.nil?
      stream = JavaLib::ByteArrayInputStream.new(bytes)
      if next_message_length == 0
        @next_message_length = read_message_length(stream)
      end
      while next_message_length >= 1 && stream.available >= next_message_length
        message_bytes = Java::byte[next_message_length].new
        stream.read(message_bytes,0,next_message_length)
        byte_messages << message_bytes
        if stream.available >= 1
          @next_message_length = read_message_length(stream)
          if next_message_length > stream.available
            @bytes = Java::byte[stream.available].new
            stream.read(@bytes,0,stream.available)
          end
        else
          reset
        end
      end
      byte_messages
    end

    def add_bytes(bytes_to_add)
      if @bytes.nil?
        @bytes = bytes_to_add
      else
        new_bytes = Java::byte[bytes_to_add.length + @bytes.length].new
        java.lang.System.arraycopy(@bytes, 0, new_bytes, 0, @bytes.length)
        java.lang.System.arraycopy(bytes_to_add, 0, new_bytes, @bytes.length, bytes_to_add.length)
        @bytes = new_bytes
      end
    end

    private

    def reset
      @next_message_length = 0
      @bytes = nil
    end

    def read_message_length(s)
      ProtoLib::CodedInput.readRawVarint32(s)
    rescue Exception => e
      raise "Error reading protobuf message length: #{e.to_s}"
    end

  end
end
