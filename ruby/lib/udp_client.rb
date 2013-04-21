

require 'socket'
import com.game_machine.messages.MessageUtil

port = 1234
host = "192.168.1.3"

s = UDPSocket.new
s.connect(host,port)
loop do
  s.send("testing",0,host,port)
  text, sender = s.recvfrom(1024)
  puts sender.inspect
  puts text
  sleep 0.020
end