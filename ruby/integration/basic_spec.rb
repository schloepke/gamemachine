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
      entity.player = player
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

    context "test" do

      describe "sending messages to remote actors" do
        it "there and back again" do
          ref = Systems::LocalEcho.find_remote('seed01')
          ref.send_message('blah', :blocking => true, :timeout => 1000).should == 'blah'
          returned_entity = ref.send_message(entity, :blocking => true, :timeout => 1000)
          returned_entity.get_id.should == entity.get_id
        end

        it "distributed messaging should return answer" do
          ref = Systems::LocalEcho.find_distributed('blah', 'DistributedLocalEcho')
          returned_entity = ref.send_message(entity, :blocking => true, :timeout => 1000)
          returned_entity.get_id.should == entity.get_id
        end

        it "udt should get response" do
          bytes = entity_list.to_byte_array
          Thread.current['s'] ||= Client.connect_udt
          result = Client.send_udt(Thread.current['s'],bytes)
          Entity.parse_from(result).id.should == entity.id
        end
      end

    end
  end
end
