require 'lib/game_machine'
require 'socket'

java_import com.game_machine.messages.MessageUtil
java_import com::game_machine::messages::ProtobufMessages::ClientMessage

#String.from_java_bytes(blob.to_java_bytes)

threads = []

1.times do
  threads << Thread.new do

    port = 1234
    host = "192.168.1.3"

    s = UDPSocket.new
    s.connect(host,port)
    
    loop do
      msg = MessageUtil.build_client_msg("test","192.168.130.128").to_byte_array
      s.send(msg.to_s,0,host,port)
      begin
        while (d = s.recvfrom_nonblock(2048))
          text = d[0]
          sender = d[1]
          #puts 'got'
          #msg = ClientMessage.parse_from(text.to_java_bytes)
          #puts "got #{msg.get_body.to_string_utf8}"
        end
      rescue IO::WaitReadable => e
        #IO.select([s],[],[],0.02)
        # retry
      end

      sleep 0.40
    end
  end
end

threads.each {|t| t.join}
