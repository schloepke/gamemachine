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

    it "mono_vm_test1" do
      namespace = 'GameMachine'
      klass = 'TestActor'
      vm = Mono::Vm.instance
      message = MessageLib::Entity.new.set_id(STR)
      puts timed = Benchmark.realtime {
      10000.times do |i|
        response = vm.send_message(namespace,klass,message)
      end
      }
    end

    xit "mono_vm_test" do
      Actor::Builder.new(MonoTest).with_router(JavaLib::RoundRobinRouter,10).start
      message = MessageLib::Entity.new.set_id(STR)
      puts timed = Benchmark.realtime {
      10000.times do |i|
        #MonoTest.find.tell(message)
        MonoTest.find.ask(message,10)
      end
      }
    end

    xit "mono loop test" do
      path = "/home/chris/game_machine/server/mono/test_actor.dll"
      Mono.load_mono(path)
      domain = Mono.create_domain('/home/chris/game_machine/server/mono/app.config')
      namespace = 'GameMachine'
      klass = 'TestActor'
      Mono.attach_current_thread(domain)
      image = Mono.load_assembly(domain,path)
      #Actor::Builder.new(MonoTest,path,'GameMachine','TestActor',domain).with_router(JavaLib::RoundRobinRouter,2).start
      Actor::Builder.new(MonoTest,path,'GameMachine','TestActor',domain,image).with_router(JavaLib::RoundRobinRouter,10).with_dispatcher("default-pinned-dispatcher").start
      Mono.set_callback(1,Mono::Callback)
      message = MessageLib::Entity.new.set_id(STR)
      100.times do
        #MonoTest.find.tell(message)
        MonoTest.find.ask(message,10)
      end
    end

    xit "can send message to ruby" do
      props = JavaLib::Props.new(Endpoints::Http::Rpc)
      Akka.instance.actor_system.actor_of(props,Endpoints::Http::Rpc.name)
      Actor::Builder.new(Endpoints::ActorUdp).start
      path = "/home/chris/game_machine/server/mono/test_actor.dll"
      Mono.load_mono(path)
      domain = Mono.create_domain('/home/chris/game_machine/server/mono/app.config')
      #Actor::Builder.new(MonoTest,path,'GameMachine','TestActor').with_router(JavaLib::RoundRobinRouter,10).start
      Actor::Builder.new(MonoTest,path,'GameMachine','TestActor',domain).with_router(JavaLib::RoundRobinRouter,10).with_dispatcher("default-pinned-dispatcher").start
      Mono.set_callback(1,Mono::Callback)
      sleep 1
      threads = []
      5.times do
        sleep 1
        threads << Thread.new do
          100000.times do
            entity = MessageLib::Entity.new.set_id(STR)
            MonoTest.find.ask(entity,10)
          end
        end
      end
      threads.map(&:join)
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
