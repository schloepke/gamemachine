module GameMachine
  module Physics
    class JmeApp < Jme::SimpleApplication

      attr_reader :app_state, :physics_space, :started

      def simpleInitApp
        @app_state = Jme::BulletAppState.new
        self.state_manager.attach(@app_state)
        @physics_space = app_state.get_physics_space
        physics_space.set_gravity(Jme::Vector3f.new(0,0,0))
        @started = true
      end

      def wait_for_start
        while (!@started)
          sleep 0.100
        end
      end

      def add_geometry(geometry)
        physics_space.add(geometry)
        self.root_node.attach_child(geometry)
      end
    end
  end
end
