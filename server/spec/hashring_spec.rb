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
        subject.nodes.sort.should == buckets.sort
      end
    end

    describe "#points" do
      it "should be number of points equal to REPLICAS * buckets.size " do
        subject.points.size.should == subject.buckets.size * Hashring::REPLICAS
      end
    end

    describe "#bucket_for" do
      it "returns bucket for value" do
        buckets.include?(subject.bucket_for('test')).should be_true
      end
    end

    describe "#remove_bucket" do
      it "removes the bucket from buckets array" do
        subject.remove_bucket('server1')
        subject.buckets.size.should == 1
        subject.buckets.first.should == 'server2'
      end

      it "removes node from ring" do
        subject.remove_bucket('server1')
        subject.nodes.size.should == 1
        subject.buckets.first.should == 'server2'
      end
    end

    describe "#add_bucket" do
      it "adds new bucket to buckets array" do
        subject.add_bucket('blah')
        subject.buckets.size.should == 3
        subject.buckets.sort.first.should == 'blah'
      end

      it "adds node to ring" do
        subject.add_bucket('blah')
        subject.nodes.size.should == 3
        subject.nodes.sort.first.should == 'blah'
      end
    end
  end
end
