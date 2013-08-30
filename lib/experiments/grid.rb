module Demo
  class Grid
    def initialize
      @count = 0
      @max = 2000
    end
    def blocked(loc,parent)
      return false if parent.nil?
      if loc.get_x >= @max || loc.get_y >= @max
        return true
      end
      if parent.get_x >= @max || parent.get_y >= @max
        return true
      end
      if loc.get_x <= 0 || loc.get_y <= 0
        return true
      end
      if parent.get_x < 0 || parent.get_y < 0
        return true
      end
      @count += 1
      if @count > @max
        true
      else
        false
      end
    end
  end
end
