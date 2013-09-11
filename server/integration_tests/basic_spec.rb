require 'integration_helper'

module GameMachine

  describe "basic" do 

    let(:client) {Clients::Client.new(:seed01)}

    context "test" do

      describe "sending messages to remote actors" do

        it "there and back again" do
          ref = GameSystems::LocalEcho.find_remote('seed01')
          ref.send_message('blah', :blocking => true, :timeout => 1000).should == 'blah'
          returned_entity = ref.send_message(static_entity, :blocking => true, :timeout => 1000)
          returned_entity.get_id.should == static_entity.get_id
        end

        it "distributed messaging should return answer" do
          ref = GameSystems::LocalEcho.find_distributed('blah', 'DistributedLocalEcho')
          returned_entity = ref.send_message(static_entity, :blocking => true, :timeout => 1000)
          returned_entity.get_id.should == static_entity.get_id
        end

        it "udt should get response" do
          bytes = static_client_message.to_byte_array
          Thread.current['s'] ||= Clients::Client.connect_udt
          result = Clients::Client.send_udt(Thread.current['s'],bytes)
          returned_message = ClientMessage.parse_from(result)
          returned_message.get_entity_list.first.id.should == static_entity.id
        end

        it "udp should get response" do
          bytes = static_client_message.to_byte_array
          c = Clients::Client.new(:seed01)
          c.send_message(bytes)
          message = c.receive_message
          returned_message = ClientMessage.parse_from(message.to_java_bytes)
          returned_message.get_entity_list.first.id.should == static_entity.id
        end

        it "player should be able to login via http" do
         response = Clients::Client.http_post('/auth',{:username => 'player', :password => 'pass'})
         response = JSON.parse(response)
         response['authtoken'].should == 'authorized'
        end

        it "write behind cache writes to couchbase" do
          ObjectDb.put(static_entity)
          ObjectDb.get(static_entity.id).id.should == static_entity.id
        end

        it "echo request" do
          client = Clients::TestClient.start('test2',8200)
          message = Helpers::GameMessage.new('player123')
          message.echo_test('one')
          client.send_to_server(message.client_message)
          expect(client.entity_with_component('EchoTest',0.900)).to be_true
          logout = Helpers::GameMessage.new('player123')
          logout.player_logout
          client.send_to_server(logout.client_message)
        end
      end

    end
  end
end
