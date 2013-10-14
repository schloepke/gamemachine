require 'spec_helper'
require_relative 'mono_test'
module GameMachine
  describe 'mono' do

    it "can send message to ruby" do
      Actor::Builder.new(Endpoints::ActorUdp).start
      image = MonoUtil.load_assembly("/home2/chris/game_machine/server/mono/test_actor.dll")
      Actor::Builder.new(MonoTest,image,'GameMachine','TestActor').start
      sleep 1
      entity = Entity.new.set_id('test')
      100.times do
        MonoTest.find.tell(entity)
      end
      sleep 2
    end
  end
end
