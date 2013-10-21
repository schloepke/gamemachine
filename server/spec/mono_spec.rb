require 'spec_helper'
require_relative 'mono_test'
require "base64"
java_import 'java.util.concurrent.ThreadPoolExecutor'
java_import 'java.util.concurrent.TimeUnit'
java_import 'java.util.concurrent.LinkedBlockingQueue'
java_import 'java.util.concurrent.FutureTask'
java_import 'java.util.concurrent.Callable'


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

    xit "tests mono http server" do
      threads = []
      8.times do
        threads << Thread.new do
          1000.times do
            response = Faraday.get 'http://localhost/'
          end
        end
      end
      threads.map(&:join)
    end

    xit "runs in java thread pool" do
      path = "/home2/chris/game_machine/server/mono/test_actor.dll"
      namespace = "GameMachine"
      klass = "TestActor"
      Mono.load_mono(path)
      Mono.attach_current_thread
      image = MonoUtil.load_assembly(path)
      entity = Entity.new.set_id('test')
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

    xit "stress test" do
      path = "/home2/chris/game_machine/server/mono/test_actor.dll"
      namespace = "GameMachine"
      klass = "TestActor"
      Mono.load_mono(path)

      threads = []
      8.times do
        threads << Thread.new do
          Mono.attach_current_thread
          image = MonoUtil.load_assembly(path)
          current_thread_id = JRuby.reference(Thread.current).native_thread.id
            entity = Entity.new.set_id('0')
            message = entity.to_byte_array
          1000000.times do
            Mono.on_receive2(image,namespace,klass,current_thread_id.to_s,message.to_s,message.size)
          end
        end
      end
      threads.map(&:join)
    end

    it "can send message to ruby" do
      Actor::Builder.new(Endpoints::ActorUdp).start
      path = "/home2/chris/game_machine/server/mono/test_actor.dll"
      Mono.load_mono(path)
      #Actor::Builder.new(MonoTest,path,'GameMachine','TestActor').with_router(JavaLib::RoundRobinRouter,10).start
      Actor::Builder.new(MonoTest,path,'GameMachine','TestActor').with_router(JavaLib::RoundRobinRouter,10).with_dispatcher("default-pinned-dispatcher").start
      sleep 1
      1.times do
      100000.times do
        entity = Entity.new.set_id('test')
        MonoTest.find.tell(entity)
        MonoTest.find.tell(entity)
        MonoTest.find.tell(entity)
        MonoTest.find.tell(entity)
        MonoTest.find.tell(entity)
        MonoTest.find.tell(entity)
        MonoTest.find.tell(entity)
        MonoTest.find.ask(entity,10)
      end
      end
      sleep 2
    end
  end
end
