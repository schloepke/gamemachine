require 'spec_helper'
require 'matrix'
module GameMachine
  describe "Grid" do

    it "workds" do
    matrix = Matrix.build(10,10) {rand}
    puts matrix.element(3,4)
    puts matrix.inspect

    end
  end
end
