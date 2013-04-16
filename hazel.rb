require 'benchmark'
require 'jars/hazelcast-all-2.5.jar'

import com.hazelcast.client.ClientConfig
import com.hazelcast.client.HazelcastClient
import com.hazelcast.core.HazelcastInstance
import com.hazelcast.core.IMap
import java.util.Map

def test
  clientConfig = ClientConfig.new
  clientConfig.addAddress("127.0.0.1:5701")
  client = HazelcastClient.newHazelcastClient(clientConfig)
  @map = client.getMap("customers")


  100.times do
    threads = []
    5.times do
      threads << Thread.new do
        puts Benchmark.realtime {
          1000.times do |x|
            @map.put(x, x)
            r = @map.get(x)
          end
        }
      end
    end
    threads.each { |t| t.join }
  end
  puts 'DONE'
end

test
