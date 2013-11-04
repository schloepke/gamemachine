require 'spec_helper'
module GameMachine
  module DataStores
    describe Redis do

      after(:each) do
        DataStore.instance.shutdown
        DataStore.instance.set_store('memory')
      end

      subject do
        instance = DataStore.instance
        instance.set_store('redis')
        instance
      end

      describe "#get" do
        it "returns the value" do
          subject.set('key','value')
          expect(subject.get('key')).to eql('value')
        end
      end

      describe "#set" do
        it "returns true" do
          expect(subject.set('key','value')).to be_true
        end
      end

      describe "#delete" do
        it "returns true" do
          expect(subject.delete('key')).to be_true
        end
      end

      describe "#shutdown" do
        it "should return nil" do
          expect(subject.shutdown).to be_nil
        end
      end
    end
  end
end

