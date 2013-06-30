require 'integration_helper'

module GameMachine

  describe "basic" do 

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
