require 'matrix'

module GameMachine
  module Physics
    class Grid

      def initialize(max,cell_size)
        @max = max
        @cell_size = cell_size
        @hash = {}
        @min = 0
        @width = @max / @cell_size
        @conv_factor = 1.0/@cell_size
        @cell_count = (@width * @width)
      end

      def neighbors(x,y,radius)
        offset = radius/@cell_size
        points = []

        1.upto(offset) do |i|
          bounds = radius * i
          points << hash(x - bounds,y - bounds)
          points << hash(x - bounds,y + bounds)

          points << hash(x + bounds,y - bounds)
          points << hash(x + bounds,y + bounds)

          points << hash(x - bounds,y)
          points << hash(x + bounds,y)

          points << hash(x,y - bounds)
          points << hash(x,y + bounds)
        end
        points.delete_if {|point| point < @min || point > (@cell_count - 1)}
        points
      end

      def hash(x,y)
        (x*@conv_factor).to_i + (y*@conv_factor).to_i * @width
      end

      def hash2(x,y)
        (x/@cell_size).floor + (y/@cell_size).floor * @width
      end

    end
  end
end
