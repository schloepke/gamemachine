require 'spec_helper'

module GameMachine
  module Commands

    describe DatastoreCommands do

      let(:entity) do
        entity = MessageLib::Entity.new
        entity.set_id('1')
        entity
      end

      subject{DatastoreCommands.new}

      before(:each) do
        subject.define_dbproc(:test1) do |entity|
          entity
        end
      end

      describe "#put" do
        it "sets the value" do
          subject.put(entity).should be_true
        end
      end

      describe "#dbproc" do
        it "call with entity id, returns entity" do
          subject.put(entity)
          sleep 0.100
            
          result = subject.call_dbproc(:test1, entity.get_id,true)
          result.should be_kind_of(MessageLib::Entity)
          result.get_id.should == entity.get_id
        end

        it "returns true when called with blocking=false" do
          subject.put(entity)
          sleep 0.100
            
          subject.call_dbproc(:test1, entity.get_id,false).should be_true
        end

        it "returns false if called with an entity id that does not exist" do
          subject.call_dbproc(:test1, 'blah',true).should be_false
        end
      end

      describe "#get" do
        it "should return false if object does not exist" do
          subject.get('xx').should be_false
        end

        it "should return object if exists" do
          subject.put(entity)
          sleep 0.100
          subject.get('1').should == entity
        end
      end
    end
  end
end
