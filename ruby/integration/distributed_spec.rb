require 'integration_helper'

module GameMachine
  CHARS = [*('a'..'z'),*('0'..'9')].flatten
  STR = Array.new(100) {|i| CHARS.sample}.join
  STR2 = Array.new(1000) {|i| CHARS.sample}.join

  describe "basic" do 


    let(:player) do
      player = Player.new
      player.set_id('2')
      player.set_authtoken('authorized')
    end

    let(:entity) do
      entity = Entity.new
      entity.set_id(STR.to_java_string)
      entity.set_player(player)
      echo_test = EchoTest.new.set_message('testing')
      entity.set_echo_test(echo_test)
      entity
    end

    let(:large_entity) do
      entity = Entity.new
      entity.set_id(STR2.to_java_string)
      entity.set_player(player)
      echo_test = EchoTest.new.set_message('testing')
      entity.set_echo_test(echo_test)
      entity
    end

    let(:entity_list) do 
      entity_list = EntityList.new
      entity_list.add_entity(entity)
      entity_list.set_player(player)
      entity_list
    end

    let(:large_entity_list) do 
      entity_list = EntityList.new
      10.times do
        entity_list.add_entity(entity)
      end
      entity_list.set_player(player)
      entity_list
    end

    let(:client) {Client.new(:seed01)}

    describe "sending messages to remote actors" do
      it "distributed messaging should return answer" do
        10.times do |i|
          ref = Systems::LocalEcho.find_distributed(i.to_s, 'DistributedLocalEcho')
          returned_entity = ref.send_message(entity, :blocking => true, :timeout => 1000)
          returned_entity.get_id.should == entity.get_id
        end
      end
    end

    describe "stress test" do
      it "distributed stress" do
        measure(10,10000) do
          Thread.current['ref'] ||= Systems::LocalEcho.find_distributed(rand(100).to_s,'DistributedLocalEcho')
          returned_entity = Thread.current['ref'].send_message(entity, :blocking => true, :timeout => 1000)
          if returned_entity 
            returned_entity.get_id.should == entity.get_id
          else
            puts 'Timeout'
          end
        end
      end
    end

  end
end
