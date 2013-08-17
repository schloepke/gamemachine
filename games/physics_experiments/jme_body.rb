module GameMachine
  module Physics
    class JmeBody

      attr_reader :geometry
      def initialize
        box = Jme::Box.new(1,1,1)
        @geometry = Jme::Geometry.new("Box", box)
        geometry.addControl(Jme::GhostControl.new)
        #control.set_gravity(Jme::Vector3f.new(0,0,0))
      end

      def control
        geometry.get_control(0)
      end

    end
  end
end

