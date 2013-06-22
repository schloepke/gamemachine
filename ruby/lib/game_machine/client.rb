module GameMachine
  class Client
 

    def initialize(server)
      @host = Settings.servers.send(server).udp.host
      @port = Settings.servers.send(server).udp.port
      @socket = UDPSocket.new
      @socket.connect(@host,@port)
    end

    def send_message(message)
      @socket.send(String.from_java_bytes(message),@host,@port)
    end

    def receive_message
      @socket.recvfrom(1024)[0]
    end

    def self.client3
      port =8100
      host = "192.168.1.2"
      message = Components.fromEntities(MessageUtil.createEchoCommand()).toByteArray()
      threads = []
      count = AtomicInteger.new
      threads << Thread.new do
        while true
          puts count.get.to_i
          sleep 5
        end
      end

      5.times do
        threads << Thread.new do
          s = UDPSocket.new
          s.connect(host,port)
          str = String.from_java_bytes(message)
          1.upto(9_000_000) do |i|
            #puts count.get.to_i if count.get.to_i % 10000 == 0
            s.send(str,host,port)
            s.recvfrom(1024)
            count.get_and_increment
            #sleep 0.010
          end
        end
      end

      threads.each {|th| th.join}

    end

    def self.client2
      Client.test
    end

    def self.client
      socket = SocketUDT.new(TypeUDT::DATAGRAM)
      socket.setBlocking(true)

      address = InetSocketAddress.new(Settings.servers.default.udp.host, Settings.servers.default.udp.port)
      socket.connect(address)
      message = Components.fromEntities(MessageUtil.createEchoCommand()).toByteArray

      1.upto(1) do |i|
        puts i if i % 10000 == 0
        puts "send " + Benchmark.realtime {socket.send(message)}.to_s
        array = Java::byte[1024].new
        puts "receive " + Benchmark.realtime {socket.receive(array)}.to_s
      end
    end
  end
end
