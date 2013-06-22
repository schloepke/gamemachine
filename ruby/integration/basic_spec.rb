require 'integration_helper'

module GameMachine

  describe "basic" do 
    let(:entity) do
      entity = Entity.new
      entity.set_id('1')
      player_connection = PlayerConnection.new
      player_connection.set_id('client1')
      player_connection.set_player(Player.new.set_id('1'))
      entity.set_player_connection(player_connection)
      entity
    end

    let(:entity_list) do 
      entity_list = EntityList.new
      entity_list.add_entity(entity)
      entity_list
    end

    let(:client) {Client.new(:app01)}

    context "test" do

      describe "sending messages to remote actors" do
        it "there and back again" do
          ref = LocalEcho.find_remote('app01','LocalEchoRemote')
          ref.send_message('blah', :blocking => true, :timeout => 1000).should == 'blah'
          returned_entity = ref.send_message(entity, :blocking => true, :timeout => 1000)
          returned_entity.should be_kind_of(Entity)
          returned_entity.get_id.should == entity.get_id
        end

        it "distributed messaging should return answer" do
          10.times do |i|
            ref = LocalEcho.find_distributed(i.to_s)
            returned_entity = ref.send_message(entity, :blocking => true, :timeout => 1000)
            returned_entity.get_id.should == entity.get_id
          end

        end
      end

      describe "sending messages via udp" do
        it "should do" do
          client.send_message(entity_list.to_byte_array)
          message = client.receive_message
          e = Entity.parse_from(message.to_java_bytes)
          e.get_id.should == entity.get_id
        end
      end

      describe "stress test" do
        it "distributed stress" do
          threads = []
          1.times do |ti|
            threads << Thread.new do
              1.times do |i|
                ref = LocalEcho.find_distributed(i.to_s)
                returned_entity = ref.send_message(entity, :blocking => true, :timeout => 1000)
                returned_entity.get_id.should == entity.get_id
              end
            end
          end
          threads.map(&:join)
          sleep 2
        end
      end

    end
  end
end
