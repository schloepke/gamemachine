require 'lib/game_machine'
require 'socket'

java_import com.game_machine.MessageUtil
java_import com::game_machine::ProtobufMessages::ClientMessage

java_import com.barchart.udt.SocketUDT
java_import com.barchart.udt.TypeUDT

socket = SocketUDT.new(TypeUDT::DATAGRAM)

#String.from_java_bytes(blob.to_java_bytes)

threads = []
count = 0
tmpcount = 0

1.times do
  threads << Thread.new do

    port =8100 
    host = "192.168.1.3"
    #host = "192.168.130.128"

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
          msg = ClientMessage.parse_from(text.to_java_bytes)
          puts "got #{msg.get_body.to_string_utf8}"
      count += 1
      tmpcount += 1
      if tmpcount >= 1000
        puts count
        tmpcount = 0
      end
        end
      rescue IO::WaitReadable => e
        #IO.select([s],[],[],0.02)
        # retry
      end

      sleep 0.80
    end
  end
end

threads.each {|t| t.join}
