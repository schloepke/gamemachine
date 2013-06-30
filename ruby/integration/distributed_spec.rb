require 'integration_helper'

module GameMachine
  describe "basic" do 
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
