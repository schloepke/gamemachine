require 'spec_helper'

module GameMachine
  module GameSystems

    describe RegionManager do

      subject do
        ref = Actor::Builder.new(RegionManager).with_name('region_manager_test').test_ref
        ref.underlying_actor
      end

      describe "#post_init" do
        it "should assign region servers" do
          expect(subject.regions['zone1']).to eq 'seed01'
          expect(subject.regions['zone2']).to eq 'seed02'
        end
      end

      describe "#regions_string" do
        it "should return correctly formatted string" do
          expected = "zone1=seed01|zone2=seed02"
          expect(subject.regions_string).to eq expected
        end
      end
    end
  end
end
