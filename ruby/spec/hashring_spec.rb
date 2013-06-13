require 'spec_helper'

module GameMachine
  describe Hashring do
    
    let(:servers) {['server1','server2']}
    let(:bucket_name) {'test_bucket'}
    let(:bucket_count) {2}
    subject do
      Hashring.new(servers,bucket_name,bucket_count)
    end

    before(:each) do
      subject.hash!
    end

    describe "#hash!" do

      it "creates the number of buckets in bucket_count" do
        subject.buckets.keys.size.should == bucket_count
      end

      it "assigns a server to each bucket" do
        subject.buckets['test_bucket0'].should == 'server1'
        subject.buckets['test_bucket1'].should == 'server2'
      end
    end

    describe "#remove_server" do
      it "should remove server from @servers" do
        subject.remove_server('server1')
        subject.servers.include?('server1').should be_false
      end

      it "removed server should not have any buckets assigned" do
        subject.remove_server('server1')
        subject.buckets.values.include?('server1').should be_false
      end

      it "buckets should get reassigned" do
        subject.buckets['test_bucket0'].should == 'server1'
        subject.remove_server('server1')
        subject.buckets['test_bucket0'].should == 'server2'
      end
    end

  end
end
