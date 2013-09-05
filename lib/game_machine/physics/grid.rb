module GameMachine
  module Physics

    # @note Thread safe 2d grid that tracks entity locations and  provides
    #   neighbor queries.  The grid stores 3d coordinates, but only calculates
    #   neighbors based on 2d.  
    # This class is thread safe.  You can create as many instances as you want.
    class Grid

      # max is the width of the grid, cell size should reflect the area
      #   that you want neighbor queries to use.
      def initialize(max,cell_size)
        @max = max
        @min = 0
        @cell_size = cell_size
        @conv_factor = 1.0/@cell_size
        @width = @max / @cell_size
        @cell_count = @width * @width
        @object_index = java.util.concurrent.ConcurrentHashMap.new
        @cells = java.util.concurrent.ConcurrentHashMap.new
        @cells_cache = java.util.concurrent.ConcurrentHashMap.new
      end

      # Returns the cells that are in range.
      def cells_within_radius(x,y,radius)
        key = "#{x}#{y}#{radius}"
        if cells = @cells_cache.get(key)
          return cells
        end

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
        result = cells.uniq
        unless @cells_cache.get(key)
          @cells_cache.put(key,result)
        end
        result
      end

      # Returns entities that are within range
      def neighbors(x,y,radius,type)
        result = {:players => [], :npcs => []}

        cells_within_radius(x,y,radius).each do |cell|
          points_in_cell(cell).each do |point|
            unless point.empty?
              point_class = point[2].class
              next if type != :all && type != point_class
              if point_class == Npc
                result[:npcs] << point[2]
              elsif point_class == Player
                result[:players] << point[2]
              end
            end
          end
        end
        result
      end

      def points_in_cell(cell)
        if points = @cells.get(cell)
          return points.values
        else
          return []
        end
      end

      def get(id)
        @object_index.get(id)
      end

      def remove(id)
        if values = @object_index.get(id)
          cell = values[1]
          if point = @cells.get(cell)
            point.remove(id)
          end
          @object_index.remove(id)
        end
      end

      # Set the location of an entity. Expects an entity with a transform
      #   and vector3 component.
      def set(entity)
        id = entity.id
        vector3 = entity.transform.vector3
        if existing_values = @object_index.fetch(id,nil)
          existing_vector3 = existing_values[2].transform.vector3
          if entity.transform.vector3.x == existing_vector3.x && entity.transform.vector3.y == existing_vector3.y
            return
          end
        end
        cell = hash(vector3.x,vector3.y)
        values = [id,cell,entity]
        @object_index[id] = values
        unless @cells.contains_key(cell)
          @cells.put(cell,java.util.concurrent.ConcurrentHashMap.new)
        end
        @cells.get(cell).put(id,values)
      end

      # Hashes an x,y pair to it's cell
      def hash(x,y)
        (x*@conv_factor).to_i + (y*@conv_factor).to_i * @width
      end

      def hash2(x,y)
        (x/@cell_size).floor + (y/@cell_size).floor * @width
      end

    end
  end
end
