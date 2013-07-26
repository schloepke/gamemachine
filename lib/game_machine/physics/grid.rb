require 'matrix'

module GameMachine
  module Physics

    class GridPoint

      attr_reader :id
      attr_accessor :x, :y
      def initialize(id,x,y)
        @id = id
        @x = x
        @y = y
      end
    end

    class Grid

      def initialize(max,cell_size)
        @max = max
        @min = 0
        @cell_size = cell_size
        @object_index = {}
        @cells = {}
      end

      def neighbors(x,y,radius)
        offset = radius/@cell_size
        cells = [hash(x,y)]

        1.upto(offset) do |i|
          bounds = radius * i
          cells << hash(x - bounds,y - bounds)
          cells << hash(x - bounds,y + bounds)

          cells << hash(x + bounds,y - bounds)
          cells << hash(x + bounds,y + bounds)

          cells << hash(x - bounds,y)
          cells << hash(x + bounds,y)

          cells << hash(x,y - bounds)
          cells << hash(x,y + bounds)
        end

        cells.delete_if {|cell| cell < @min || cell > (cell_count - 1)}
        cells.uniq!
        cells.map do |cell|
          points = points_in_cell(cell)
          points.empty? ? nil : points
        end.compact.flatten
      end

      def points_in_cell(cell)
        @cells.fetch(cell,{}).values
      end

      def get(id)
        @object_index.fetch(id,nil)
      end

      def remove(id)
        if cell = @object_index.fetch(id,nil)
          if @cells.has_key?(cell)
            @cells[cell].delete(id)
          end
          @object_index.delete(id)
        end
      end

      def set(id,x,y)
        cell = hash(x,y)
        @object_index[id] = cell
        @cells[cell] ||= {}
        @cells[cell][id] = GridPoint.new(id,x,y)
      end

      private

      def conv_factor
        1.0/@cell_size
      end

      def width
        @max / @cell_size
      end

      def cell_count
        width * width
      end

      def hash(x,y)
        (x*conv_factor).to_i + (y*conv_factor).to_i * width
      end

      def hash2(x,y)
        (x/@cell_size).floor + (y/@cell_size).floor * width
      end

    end
  end
end
