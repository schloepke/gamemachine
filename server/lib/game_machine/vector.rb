module GameMachine
  class Vector

    attr_accessor :x, :y, :z

    def self.from_array(ar)
      new(ar[0],ar[1],ar[2])
    end

    def self.from(v)
      new(v.x,v.y,v.z)
    end

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
      #dz = @z - v.z
      #(dx * dx + dy * dy + dz * dz);
      (dx * dx + dy * dy);
    end

    def distance(v)
      Math.sqrt(distance_squared(v))
    end

    def lerp(vector,percent)
      @x = lerp_float(x,vector.x,percent)
      @y = lerp_float(y,vector.y,percent)
    end

    def lerp_float(start_loc,end_loc,percent)
      start_loc + (percent * (end_loc - start_loc))
    end

    def interpolate(vector,change_amount)
      @x=(1-change_amount)*@x + change_amount*vector.x
      @y=(1-change_amount)*@y + change_amount*vector.y
      #@z=(1-change_amount)*@z + change_amount*vector.z
    end

    def inspect
      "#{x} #{y} #{z}"
    end
  end
end
