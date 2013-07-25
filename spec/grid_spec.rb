require 'spec_helper'
require 'matrix'
module GameMachine
  describe "Grid" do

    it "workds" do
      grid = Physics::Grid.new(100,25)
      puts grid.hash(30,30)
      puts grid.neighbors(30,30,50).inspect
      puts Benchmark.realtime { 100_000.times { grid.neighbors(30,30,50) } }
      #puts Benchmark.realtime { 10_000.times {grid.hash(200,3)} }
      #puts Benchmark.realtime { 10_000.times {grid.hash2(200,3)} }
    end
  end
end
