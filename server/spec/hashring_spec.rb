require 'spec_helper'

module GameMachine
  describe 'JavaLib::Hashring' do

    let(:nodes) {['server1','server2']}
    let(:node_name) {'test_node'}

    subject do
      JavaLib::Hashring.new('test',nodes,3)
    end

    describe "#node_for" do
      it "returns node for value" do
        expect(nodes.to_a.include?(subject.node_for('test'))).to be_truthy
      end
    end

    describe "#remove_node" do
      
      it "removes node from ring" do
        subject.remove_node('server1')
        expect(subject.nodes.to_a.size).to eq(1)
        expect(subject.nodes.to_a.first).to eq('server2')
      end
    end

    describe "#add_node" do
      it "adds new node to nodes array" do
        subject.add_node('blah')
        expect(subject.nodes.to_a.size).to eq(3)
        expect(subject.nodes.to_a.sort.first).to eq('blah')
      end

    end
  end
end
