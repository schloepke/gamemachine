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

class TaskRunner
  include Callable

  def self.executor
    core_pool_size = 5
    maximum_pool_size = 5
    keep_alive_time = 300 # keep idle threads 5 minutes around
    ThreadPoolExecutor.new(core_pool_size, maximum_pool_size, keep_alive_time, TimeUnit::SECONDS, LinkedBlockingQueue.new)
  end

  def initialize(image,namespace,klass,message)
    @image = image
    @namespace = namespace
    @klass = klass
    @message = message
  end

  def call
    Mono.attach_current_thread
    current_thread_id = JRuby.reference(Thread.current).native_thread.id
    Mono.on_receive2(@image,@namespace,@klass,current_thread_id.to_s,@message.to_s,@message.size)
  end
end

module GameMachine
  describe 'mono' do

    xit "runs in java thread pool" do
      path = "/home/chris/game_machine/server/mono/test_actor.dll"
      namespace = "GameMachine"
      klass = "TestActor"
      Mono.load_mono(path)
      Mono.attach_current_thread
      image = MonoUtil.load_assembly(path)
      entity = MessageLib::Entity.new.set_id('test')
      message = entity.to_byte_array
      puts message.to_s.encoding
      str = String.from_java_bytes(message)
      message = Base64.encode64(str)
      executor = TaskRunner.executor
      1000000.times do
        tasks = []
        5.times do 
          task = FutureTask.new(TaskRunner.new(image,namespace,klass,message))
          executor.execute(task)
          tasks << task
        end

        tasks.each do |t|
          t.get
        end
      end
      executor.shutdown
    end

    xit "jruby thread pool" do
      path = "/home/chris/game_machine/server/mono/test_actor.dll"
      Mono.load_mono(path)
      domain = Mono.create_domain('/home/chris/game_machine/server/mono/app.config')
      namespace = 'GameMachine'
      klass = 'TestActor'
      Mono.attach_current_thread(domain)
      Mono.set_callback(1,Mono::Callback)

      threads = []
      5.times do
        threads << Thread.new do
          Mono.attach_current_thread(domain)
          image = Mono.load_assembly(domain,path)
          message = MessageLib::Entity.new.set_id(STR2)
          1000000.times do
            GameMachine::Actor::MonoActor.call_mono(message,image,domain,namespace,klass)
          end
        end
      end
      threads.map(&:join)
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
        Net::HTTP.start("192.168.1.6",8888) do |http|
          10000.times do
            #http.get("/actor/message")
            puts http.post("/actor/message","name=test")
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
