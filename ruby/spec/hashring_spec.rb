require 'spec_helper'

module GameMachine
  describe Hashring do

    let(:servers) {['server1','server2']}
    let(:bucket_name) {'test_bucket'}
    subject do
      Hashring.new(servers)
    end

    describe "#bucket_for" do
      it "returns bucket for value" do
        subject.bucket_for('test')
      end
    end
  end
end
