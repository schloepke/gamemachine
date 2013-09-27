module GameMachine
  class Vector

    attr_accessor :x, :y, :z

    def initialize(x=0.0,y=0.0,z=0.0)
      @x = x
      @y = y
      @z = z
    end

    def==(v)
      @x == v.x && @y = v.y && @z = v.z
    end

    def zero?
      @x == 0 && @y == 0 && z == 0
    end

    def zero
      @x = 0.0
      @y = 0.0
      @z = 0.0
    end

    def set(new_x,new_y,new_z)
      @x = new_x
      @y = new_y
      @z = new_z
    end

    def distance_squared(v)
      dx = @x - v.x
      dy = @y - v.y
      dz = @z - v.z
      (dx * dx + dy * dy + dz * dz);
    end

    def distance(v)
      Math.sqrt(distance_squared(v))
    end

    def interpolate(vector,change_amount)
      @x=(1-change_amount)*@x + change_amount*vector.x
      @y=(1-change_amount)*@y + change_amount*vector.y
      @z=(1-change_amount)*@z + change_amount*vector.z
    end

    def inspect
      "#{x} #{y} #{z}"
    end
  end
end
