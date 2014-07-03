require 'spec_helper'

module GameMachine
  module Commands

    describe DatastoreCommands do

      let(:entity) do
        entity = MessageLib::Entity.new
        entity.set_id('1')
        entity
      end

      let(:entity2) do
        entity = MessageLib::Entity.new
        entity.set_id('2')
        entity
      end

      subject{DatastoreCommands.new}

      before(:each) do
        subject.define_dbproc(:test1) do |id,current_entity,update_entity|
          expect(update_entity).to eql(entity)
          current_entity
        end
        subject.define_dbproc(:test2) do |id,current_entity,update_entity|
          current_entity.set_entity_type('dunno')
          current_entity
        end
      end

      describe "#put" do
        it "sets the value" do
          expect(subject.put(entity)).to be_truthy
        end
      end

      describe "#dbproc" do
        it "call with entity id and update entity, returns entity" do
          subject.put(entity)
          sleep 0.100
            
          result = subject.call_dbproc(:test1, entity.get_id,entity,true)
          expect(result).to be_kind_of(MessageLib::Entity)
          expect(result.get_id).to eq(entity.get_id)
        end

        it "returns updated entity" do
          subject.put(entity)
          sleep 0.100
            
          result = subject.call_dbproc(:test2, entity.get_id,entity,true)
          expect(result.get_entity_type).to eq('dunno')
        end

        it "returns true when called with blocking=false" do
          subject.put(entity)
          sleep 0.100
            
          expect(subject.call_dbproc(:test1, entity.get_id,entity,false)).to be_truthy
        end

      end

      describe "#get" do
        it "should return false if object does not exist" do
          expect(subject.get('xx')).to be_falsey
        end

        it "should return object if exists" do
          subject.put(entity)
          sleep 0.100
          expect(subject.get('1')).to eq(entity)
        end
      end

      describe "#delete" do
        it "removes the entity from the data store" do
          subject.put(entity)
          subject.delete(entity.id)
          expect(subject.get('1')).to be_falsey
        end
      end
    end
  end
end
