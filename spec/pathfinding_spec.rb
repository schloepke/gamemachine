require 'spec_helper'

module GameMachine
  class Grid
    def initialize
      @count = 0
    end
    def blocked(loc,parent)
      return false if parent.nil?
      #puts "#{loc} #{parent}"
      if loc.get_x >= 1000 || loc.get_y >= 1000
        return true
      end
      if parent.get_x >= 1000 || parent.get_y >= 1000
        return true
      end
      if loc.get_x <= 0 || loc.get_y <= 0
        return true
      end
      if parent.get_x < 0 || parent.get_y < 0
        return true
      end
      @count += 1
      if @count > 8000
        true
      else
        false
      end
    end
  end
  describe 'pathfinding' do

    it "works" do
      node1 = JavaLib::Node.new(0,0)
      node2 = JavaLib::Node.new(390,390)
      finder = JavaLib::Pathfinding.new(JavaLib::Pathfinding::Algorithm::ASTAR,Grid.new)
      finder.set_eight(true)
      #finder.setHeuristic(JavaLib::Pathfinding::Heuristic::EUCLIDEAN)
      result = nil
      time = Benchmark.realtime do
      100000.times do
        result = finder.find_path(node1,node2)
      end
      end
      puts "#{time} "
    end
  end
end

