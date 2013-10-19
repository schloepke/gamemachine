require 'spec_helper'
require_relative 'mono_test'
module GameMachine
  describe 'mono' do

    xit "stress test" do
      path = "/home2/chris/game_machine/server/mono/test_actor.dll"
      namespace = "GameMachine"
      klass = "TestActor"
      Mono.load_mono(path)

      threads = []
      4.times do
        threads << Thread.new do
          Mono.attach_current_thread
          image = MonoUtil.load_assembly(path)
          mono_object = Mono.create_object(image,namespace,klass)
          100000.times do
            entity = Entity.new.set_id('0')
            message = entity.to_byte_array
            Mono.on_receive(mono_object,message.to_s,message.size)
          end
        end
      end
      threads.map(&:join)
    end

    it "can send message to ruby" do
      Actor::Builder.new(Endpoints::ActorUdp).start
      path = "/home2/chris/game_machine/server/mono/test_actor.dll"
      Mono.load_mono(path)
      #Actor::Builder.new(MonoTest,path,'GameMachine','TestActor').start#.with_router(JavaLib::RoundRobinRouter,10).start
      Actor::Builder.new(MonoTest,path,'GameMachine','TestActor').with_router(JavaLib::RoundRobinRouter,10).with_dispatcher("default-pinned-dispatcher").start
      sleep 1
      1.times do
      1.times do
        entity = Entity.new.set_id('test')
        #MonoTest.find.tell(entity)
        MonoTest.find.ask(entity,10)
      end
      sleep 2
      end
      sleep 2
    end
  end
end
