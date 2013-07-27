
require 'integration_helper'

class ObjectDbCallbacks
  include GameMachine::ObjectDbProc

    dbproc(:update) do |entity|
      entity
    end
end

module GameMachine
  describe ObjectDb do 

    describe "stress" do

      it "stress with small payload" do
        measure(10,10000) do
          e = entity
          id = e.id
          ObjectDb.put(e)
          if returned_entity = ObjectDb.get(id)
            returned_entity.id.should == id
          end
        end
      end

      it "stress get" do
        measure(10,10000) do
          e = entity
          id = e.id
          if returned_entity = ObjectDb.get(id)
            returned_entity.id.should == id
          end
        end
      end

      it "stress dbproc" do
        measure(10,10000) do
          e = entity
          id = e.id
          if returned_entity = ObjectDb.call_dbproc(:update, e.id,true)
            returned_entity.id.should == id
          end
        end
      end

      it "stress with large payload" do
        measure(10,10000) do
          e = large_entity
          id = e.id
          ObjectDb.put(e)
          if returned_entity = ObjectDb.get(id)
            returned_entity.id.should == id
          end
        end
      end
    end

  end
end

