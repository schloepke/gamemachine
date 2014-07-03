require 'spec_helper'

module GameMachine
  describe Hashring do

    let(:buckets) {['server1','server2']}
    let(:bucket_name) {'test_bucket'}

    subject do
      Hashring.new(buckets)
    end

    describe "#nodes" do
      it "nodes should equal buckets" do
        expect(subject.nodes.sort).to eq(buckets.sort)
      end
    end

    describe "#points" do
      it "should be number of points equal to REPLICAS * buckets.size " do
        expect(subject.points.size).to eq(subject.buckets.size * Hashring::REPLICAS)
      end
    end

    describe "#bucket_for" do
      it "returns bucket for value" do
        expect(buckets.include?(subject.bucket_for('test'))).to be_truthy
      end
    end

    describe "#remove_bucket" do
      it "removes the bucket from buckets array" do
        subject.remove_bucket('server1')
        expect(subject.buckets.size).to eq(1)
        expect(subject.buckets.first).to eq('server2')
      end

      it "removes node from ring" do
        subject.remove_bucket('server1')
        expect(subject.nodes.size).to eq(1)
        expect(subject.buckets.first).to eq('server2')
      end
    end

    describe "#add_bucket" do
      it "adds new bucket to buckets array" do
        subject.add_bucket('blah')
        expect(subject.buckets.size).to eq(3)
        expect(subject.buckets.sort.first).to eq('blah')
      end

      it "adds node to ring" do
        subject.add_bucket('blah')
        expect(subject.nodes.size).to eq(3)
        expect(subject.nodes.sort.first).to eq('blah')
      end
    end
  end
end
