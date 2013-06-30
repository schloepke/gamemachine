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
      100.times do
        entity_list.add_entity(entity)
      end
      entity_list.set_player(player)
      entity_list
    end

    let(:large_entity_list) do 
      entity_list = EntityList.new
      entity_list.add_entity(large_entity)
      entity_list.set_player(player)
      entity_list
    end

    let(:client) {Client.new(:seed01)}

    context "test" do

      describe "udt" do

        it "should send/receive" do

          threads = []
          1.times do |ti|
            threads << Thread.new do
              Client.udt_test(large_entity_list.to_byte_array)
            end
          end
          threads.map(&:join)

        end
      end

      describe "sending messages to remote actors" do
        it "there and back again" do
          ref = Systems::LocalEcho.find_remote('seed01')
          ref.send_message('blah', :blocking => true, :timeout => 1000).should == 'blah'
          returned_entity = ref.send_message(entity, :blocking => true, :timeout => 1000)
          returned_entity.should be_kind_of(Entity)
          returned_entity.get_id.should == entity.get_id
        end

        it "distributed messaging should return answer" do
          10.times do |i|
            ref = Systems::LocalEcho.find_distributed(i.to_s, 'DistributedLocalEcho')
            returned_entity = ref.send_message(entity, :blocking => true, :timeout => 1000)
            returned_entity.get_id.should == entity.get_id
          end

        end
      end

      describe "sending messages via udp" do
        it "should do" do

          threads = []
          1.times do |ti|
            threads << Thread.new do
              c = Client.new(:seed01)
              100.times do |i|
                c.send_message(entity_list.to_byte_array)
                message = c.receive_message
                e = Entity.parse_from(message.to_java_bytes)
                #e.get_id.should == entity.get_id
              end
            end
          end
          threads.map(&:join)

        end
      end

      describe "stress test" do
        it "distributed stress" do
          threads = []
          1.times do |ti|
            threads << Thread.new do
              100.times do |i|
                ref = Systems::LocalEcho.find_distributed(i.to_s,'DistributedLocalEcho')
                returned_entity = ref.send_message(entity, :blocking => true, :timeout => 1000)
                returned_entity.get_id.should == entity.get_id
              end
            end
          end
          threads.map(&:join)
        end
      end

    end
  end
end
