
require 'integration_helper'

module GameMachine
  describe "objectdb" do 

    describe "stress" do
      it "stress with small payload" do
        measure(10,100000) do
          ObjectDb.put(entity)
          ObjectDb.get(entity.id)
        end
      end

      it "stress with large payload" do
        measure(10,100000) do
          ObjectDb.put(large_entity)
          ObjectDb.get(large_entity.id)
        end
      end
    end

  end
end

