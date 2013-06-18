require 'spec_helper'

module GameMachine

  describe ObjectDb do

    describe "#get" do
      it "should return a stored entity" do
        ObjectDb.get(1).should be_nil
      end
    end
  end
end
