require 'integration_helper'

module GameMachine

  describe "basic" do 

    let(:client) {Client.new(:seed01)}

    context "test" do

      describe "sending messages to remote actors" do
        it "there and back again" do
          ref = Systems::LocalEcho.find_remote('seed01')
          ref.send_message('blah', :blocking => true, :timeout => 1000).should == 'blah'
          returned_entity = ref.send_message(static_entity, :blocking => true, :timeout => 1000)
          returned_entity.get_id.should == static_entity.get_id
        end

        it "distributed messaging should return answer" do
          ref = Systems::LocalEcho.find_distributed('blah', 'DistributedLocalEcho')
          returned_entity = ref.send_message(static_entity, :blocking => true, :timeout => 1000)
          returned_entity.get_id.should == static_entity.get_id
        end

        it "udt should get response" do
          bytes = static_client_message.to_byte_array
          Thread.current['s'] ||= Client.connect_udt
          result = Client.send_udt(Thread.current['s'],bytes)
          returned_message = ClientMessage.parse_from(result)
          returned_message.get_entity_list.first.id.should == static_entity.id
        end

        it "udp should get response" do
          bytes = static_client_message.to_byte_array
          c = Client.new(:seed01)
          c.send_message(bytes)
          message = c.receive_message
          returned_message = ClientMessage.parse_from(message.to_java_bytes)
          returned_message.get_entity_list.first.id.should == static_entity.id
        end

        it "player should be able to login via http" do
         response = Client.http_post('/auth',{:username => 'player', :password => 'pass'})
         response = JSON.parse(response)
         response['authtoken'].should == Settings.authtoken
        end

        it "write behind cache writes to couchbase" do
          ObjectDb.put(static_entity)
          ObjectDb.get(static_entity.id).id.should == static_entity.id
        end

        it "join chat request" do
          GameMachine::TestClient.start('test1',8200)
          #GameMachine::TestClient.start('test1',8202)
          GameMachine::TestClient.should_receive_component('ChatChannels','test1') do
            message = GameMachine::Helpers::GameMessage.new('player1')
            message.join_chat('mygroup')
            GameMachine::TestClient.send_to_server('test1',message.client_message)
          end
        end

        it "echo request" do
          TestClient.start('test2',8200)
          TestClient.should_receive_component('EchoTest','test2') do
            message = Helpers::GameMessage.new('player1')
            message.echo_test('one')
            TestClient.send_to_server('test2',message.client_message)
          end
        end
      end

    end
  end
end
