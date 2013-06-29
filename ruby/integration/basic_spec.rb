require 'integration_helper'

module GameMachine
  CHARS = [*('a'..'z'),*('0'..'9')].flatten
  STR = Array.new(100) {|i| CHARS.sample}.join

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

    let(:entity_list) do 
      entity_list = EntityList.new
      entity_list.add_entity(entity)
      entity_list.set_player(player)
      entity_list
    end

    let(:client) {Client.new(:seed01)}

    context "test" do

      describe "sending messages to remote actors" do
        it "there and back again" do
          ref = LocalEcho.find_remote('seed01')
          ref.send_message('blah', :blocking => true, :timeout => 1000).should == 'blah'
          returned_entity = ref.send_message(entity, :blocking => true, :timeout => 1000)
          returned_entity.should be_kind_of(Entity)
          returned_entity.get_id.should == entity.get_id
        end

        it "distributed messaging should return answer" do
          10.times do |i|
            ref = LocalEcho.find_distributed(i.to_s, 'DistributedLocalEcho')
            returned_entity = ref.send_message(entity, :blocking => true, :timeout => 1000)
            returned_entity.get_id.should == entity.get_id
          end

        end
      end

      describe "sending messages via udp" do
        it "should do" do

          threads = []
          5.times do |ti|
            threads << Thread.new do
              c = Client.new(:seed01)
              10000.times do |i|
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
          5.times do |ti|
            threads << Thread.new do
              100000.times do |i|
                ref = LocalEcho.find_distributed(i.to_s,'DistributedLocalEcho')
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
