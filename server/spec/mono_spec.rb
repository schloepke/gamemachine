require 'spec_helper'
require_relative 'mono_test'
require "base64"
require 'socket'
require "uri"
require 'net/http'
java_import 'java.util.concurrent.ThreadPoolExecutor'
java_import 'java.util.concurrent.TimeUnit'
java_import 'java.util.concurrent.LinkedBlockingQueue'
java_import 'java.util.concurrent.FutureTask'
java_import 'java.util.concurrent.Callable'

CHARS = [*('a'..'z'),*('0'..'9')].flatten
STR = Array.new(100) {|i| CHARS.sample}.join
STR2 = Array.new(1000) {|i| CHARS.sample}.join


module GameMachine
  describe 'mono' do

    it "message gateway" do
      Actor::Builder.new(Endpoints::MessageGateway).start
      sleep 1
      puts GameSystems::Devnull.to_s
      str = Array.new(1000) {|i| CHARS.sample}.join
      message = MessageLib::Entity.new.set_id(STR)
      message.set_message_routing(
       MessageLib::MessageRouting.new.set_destination('GameMachine.TestActor')
      )
      puts timed = Benchmark.realtime {
        100.times do
          Endpoints::MessageGateway.find.tell(message)
        end
      }
      sleep 2
    end

    xit "no_threadpool" do
      namespace = 'GameMachine'
      klass = 'TestActor'
      vm = Mono::Vm.instance
      str = Array.new(1000) {|i| CHARS.sample}.join
      message = MessageLib::Entity.new.set_id(STR)
      puts timed = Benchmark.realtime {
        10000.times do |i|
          response = vm.call_mono(namespace,klass,message)
        end
      }
    end
    xit "mono_vm_test1" do
      namespace = 'GameMachine'
      klass = 'TestActor'
      vm = Mono::Vm.instance
      str = Array.new(1000) {|i| CHARS.sample}.join
      message = MessageLib::Entity.new.set_id(STR)
      puts timed = Benchmark.realtime {
        10.times do |i|
          response = vm.send_message(namespace,klass,message)
        end
      }
    end

    xit "mono_vm_test" do
      Actor::Builder.new(MonoTest).with_router(JavaLib::RoundRobinRouter,10).start
      message = MessageLib::Entity.new.set_id(STR2)
      puts timed = Benchmark.realtime {
      10000.times do |i|
        #MonoTest.find.tell(message)
        MonoTest.find.ask(message,10)
      end
      }
    end

    xit "stress test mono http" do
      puts 'starting'
      threads = []
      10.times do
        threads << Thread.new do
        Net::HTTP.start("127.0.0.1",8888) do |http|
          1000.times do
            res = http.get("/actor/message")
            #puts http.post("/actor/message","name=test")
          end
        end
        end
      end
      threads.map(&:join)
    end

    xit "function callback" do
      path = "/home/chris/game_machine/server/mono/test_actor.dll"
      namespace = "GameMachine"
      klass = "TestActor"
      Mono.load_mono(path)
      Mono.do_work(1,Mono::Callback)
    end
  end
end
