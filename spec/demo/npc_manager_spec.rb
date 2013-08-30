require 'spec_helper'

module Demo
  def self.move_to(start_time,position,target_position)
    direction = target_position.subtract(position)
    direction.y = 0
    puts "direction magnitude=#{direction.length}"
    position = position.interpolate(target_position,10 * start_time)
    puts "position=#{position}"
  end

  describe SingletonManager do
      target_position = Jme::Vector3f.new(222,1.1,1000)
      position = Jme::Vector3f.new(0,0,0)
      start_time = 0
      20.times do
        Demo.move_to(start_time,position,target_position)
        start_time += 0.0001
      end
  end
end
