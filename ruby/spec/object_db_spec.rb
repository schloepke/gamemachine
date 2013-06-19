require 'spec_helper'

module GameMachine

  class ObjectdbTest

    def initialize

    end

    def call(entity)

    end
  end

  describe ObjectDb do

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

    describe "#update" do
      it "calls block with entity, and returns true" do
        ObjectDb.put(entity)
        sleep 0.100
        ObjectDb.update(entity.get_id,ObjectdbTest.name,'call').should be_true
      end
    end

    describe "#get" do
      it "should return false if object does not exist" do
        ObjectDb.get('1').should be_false
      end

      it "should return object if exists" do
        ObjectDb.put(entity)
        sleep 0.100
        ObjectDb.get('1').should == entity
      end
    end
  end
end
