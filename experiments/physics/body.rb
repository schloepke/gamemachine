module GameMachine
  module Physics
    class Body

      attr_reader :body
      def initialize
        @body = Dyn4j::Body.new
        box = Dyn4j::Rectangle.new(10,10)
        fixture = Dyn4j::BodyFixture.new(box)
        body.add_fixture(fixture)
        body.set_mass(Dyn4j::Mass::Type::FIXED_LINEAR_VELOCITY)
        self.gravity = 0
        self.set_velocity(0,0)
        @speed = 40
      end

      def move_to(x,y,speed=@speed)
        origin = Dyn4j::Vector2.new(self.x,self.y)
        dest = Dyn4j::Vector2.new(x,y)
        ray = Dyn4j::Ray.new(origin,dest)
        angle = ray.get_direction
        scale_x = Math.cos(angle)
        scale_y = Math.sin(angle)
        set_velocity(scale_x * speed,scale_y * speed)
      end

      def mass
        body.get_mass
      end

      def gravity=(scale)
        body.set_gravity_scale(scale)
      end

      def gravity
        body.get_gravity_scale
      end

      def translate(x,y)
        body.translate(x,y)
      end

      def set_velocity(x,y)
        velocity_vector = Dyn4j::Vector2.new(x,y)
        body.set_velocity(velocity_vector)
      end

      def velocity
        body.get_velocity
      end

      def center
        body.get_world_center
      end

      def x
        center.x
      end

      def y
        center.y
      end

    end
  end
end
