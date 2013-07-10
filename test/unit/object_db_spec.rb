require 'spec_helper'

module GameMachine


  describe ObjectDb do
    include GameMachine::ObjectDbProc

      dbproc(:test1) do |entity|
        entity
      end

    let(:entity) do
      entity = Entity.new
      entity.set_id('1')
      entity
    end

    describe "#put" do
      it "sets the value" do
        ObjectDb.put(entity).should be_true
      end
    end

    describe "#dbproc" do
      it "call with entity id, returns entity" do
        ObjectDb.put(entity)
        sleep 0.100
          
        result = ObjectDb.call_dbproc(:test1, entity.get_id,true)
        result.should be_kind_of(Entity)
        result.get_id.should == entity.get_id
      end

      it "returns true when called with blocking=false" do
        ObjectDb.put(entity)
        sleep 0.100
          
        ObjectDb.call_dbproc(:test1, entity.get_id,false).should be_true
      end

      it "returns false if called with an entity id that does not exist" do
        ObjectDb.call_dbproc(:test1, 'blah',true).should be_false
      end
    end

    describe "#get" do
      it "should return false if object does not exist" do
        ObjectDb.get('xx').should be_false
      end

      it "should return object if exists" do
        ObjectDb.put(entity)
        sleep 0.100
        ObjectDb.get('1').should == entity
      end
    end
  end
end
