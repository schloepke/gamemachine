module GameMachine
  class Client

    def encode_msg(msg)
      Protobuf::Field::VarintField.encode(msg.to_s.size) + msg.serialize_to_string
    end
  
    def test
      threads = []
      msg = GameSocket::Msg.new
      msg.name = "test"
      msg = encode_msg(msg)

      1.times do
        sleep 4
        1000.times do
          threads << Thread.new do
            sock = TCPSocket.new('localhost',8080)
            10000.times do
              begin
                sock.write(msg)
                #puts "data sent"
                #Timeout.timeout(0.020) do
                res = sock.recv(1)
                len = Protobuf::Field::VarintField.decode(res.bytes)
                res = sock.recv(len)
                #end
                msg2 = GameSocket::Msg.new
                msg2.parse_from_string(res)
                #puts msg.inspect
              rescue SocketError, Timeout::Error, IOError, Errno::ECONNREFUSED => e
                puts e
                sleep (rand(5) + 1)
                sock.close
                sock = TCPSocket.new('localhost',8080)
              end
              sleep (rand(1) + 1)
            end
            sock.close
          end
        end
      end
      threads.each {|th| th.join}
    end
  end

end




