
require 'socket'

s = UDPSocket.new
s.bind("192.168.130.128", 11000)
loop do
  text, sender = s.recvfrom(1024)
  puts sender.inspect
  puts text
  s.send("testing",0,sender[3],sender[1])
end
