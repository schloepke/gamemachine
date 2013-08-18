module GameMachine
  module Physics

    class Grid

      def initialize(max,cell_size)
        @max = max
        @min = 0
        @cell_size = cell_size
        @conv_factor = 1.0/@cell_size
        @width = @max / @cell_size
        @cell_count = @width * @width
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

        cells.delete_if {|cell| cell < @min || cell > (@cell_count - 1)}
        cells.uniq!
        cells.map do |cell|
          points = points_in_cell(cell)
          points.empty? ? nil : points
        end.compact.flatten(1)
      end

      def points_in_cell(cell)
        @cells.fetch(cell,{}).values
      end

      def get(id)
        @object_index.fetch(id,nil)
      end

      def remove(id)
        if values = @object_index.fetch(id,nil)
          cell = values[1]
          if @cells.has_key?(cell)
            @cells[cell].delete(id)
          end
          @object_index.delete(id)
        end
      end

      def set(entity)
        id = entity.id
        vector3 = entity.transform.vector3
        cell = hash(vector3.x,vector3.y)
        values = [id,cell,entity]
        @object_index[id] = values
        @cells[cell] ||= {}
        @cells[cell][id] = values
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
