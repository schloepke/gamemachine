
require 'integration_helper'

module GameMachine
  describe "objectdb" do 

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

