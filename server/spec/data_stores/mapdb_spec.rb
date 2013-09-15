require 'spec_helper'
module GameMachine
  module DataStores
    describe Mapdb do

      after(:each) do
        DataStore.instance.shutdown
        DataStore.instance.set_store('memory')
      end

      subject do
        instance = DataStore.instance
        instance.set_store('mapdb')
        instance
      end

      let(:entity) do
        Entity.new.set_id('one')
      end

      describe "get_and_set" do
        it "get should return the value that was set" do
          subject.set('test',entity.to_byte_array)
          entity = Entity.parse_from(subject.get('test'))
          expect(entity.id).to eq('one')
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

