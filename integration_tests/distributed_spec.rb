require 'integration_helper'

module GameMachine
  describe "basic" do 
    let(:client) {Client.new(:seed01)}

    describe "sending messages to remote actors" do
      it "distributed messaging should return answer" do
         e = entity.clone
        10.times do |i|
          ref = GameSystems::LocalEcho.find_distributed(i.to_s, 'DistributedLocalEcho')
          returned_entity = ref.send_message(e, :blocking => true, :timeout => 1000)
          returned_entity.get_id.should == e.id
        end
      end
    end

    describe "stress test" do
      it "distributed stress" do
        e = entity.clone
        measure(10,10000) do
          Thread.current['ref'] ||= GameSystems::LocalEcho.find_distributed(rand(100).to_s,'DistributedLocalEcho')
          returned_entity = Thread.current['ref'].send_message(e, :blocking => true, :timeout => 1000)
          if returned_entity 
            returned_entity.id.should == e.id
          else
            puts 'Timeout'
          end
        end
      end
    end

  end
end
