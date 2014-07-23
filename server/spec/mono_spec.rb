require 'spec_helper'
require_relative 'mono_test'
require "base64"
require 'socket'
require "uri"
require 'net/http'
require 'benchmark'
require 'descriptive_statistics'
java_import 'java.util.concurrent.ThreadPoolExecutor'
java_import 'java.util.concurrent.TimeUnit'
java_import 'java.util.concurrent.LinkedBlockingQueue'
java_import 'java.util.concurrent.FutureTask'
java_import 'java.util.concurrent.Callable'

CHARS = [*('a'..'z'),*('0'..'9')].flatten
STR = Array.new(200) {|i| CHARS.sample}.join
STR2 = Array.new(1000) {|i| CHARS.sample}.join


module GameMachine
  describe 'mono' do

    it "udp gateway" do
      host = '127.0.0.1'
      port = 4000
      message = MessageLib::Entity.new.set_id(STR)

      threads = []
      1.times do
        threads <<  Thread.new do
          results = []
          client = JavaLib::UdpClient.new(port,1)
            100000.times do
              results << Benchmark.realtime {
                client.send(message)
              }
            end
            puts "Number = #{results.number} Average #{results.mean * 1000000} Standard deviation #{results.standard_deviation}"

        end
      end
      threads.map(&:join)

    end

    xit "calls mono method" do

      Mono::Vm.instance.load

      threads = []
      4.times do
        threads <<  Thread.new do
          str = Array.new(400) {|i| CHARS.sample}.join
          message = MessageLib::Entity.new.set_id(str)
          puts Benchmark.realtime {
          1000000.times do
            Mono::Vm.instance.call_mono('GameMachine','TestActor',message)
          end
          }
        end
      end
      threads.map(&:join)
    end

    xit "mono gateway" do
      Actor::Builder.new(Endpoints::MonoGateway).start
      sleep 1
      str = Array.new(1000) {|i| CHARS.sample}.join
      message = MessageLib::Entity.new.set_id(STR)
      message.set_destination('GameMachine.TestActor')
      puts timed = Benchmark.realtime {
        10.times do
          Endpoints::MonoGateway.find.tell(message)
        end
      }
      sleep 2
    end

  end
end
