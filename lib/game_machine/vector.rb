module GameMachine
  class Vector

    attr :x, :y, :z

    def initialize(x=0.0,y=0.0,z=0.0)
      @x = x
      @y = y
      @z = z
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
