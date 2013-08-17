module GameMachine
  module Physics
    class  World3d

      attr_reader :world
      def initialize
        gravity = Gdx::Vector3.new(0,0,0)
        collisionConfiguration = Gdx::DefaultCollisionConfiguration.new
        dispatcher = Gdx::CollisionDispatcher.new(collisionConfiguration)
        broadphase = Gdx::DbvtBroadphase.new
        solver = Gdx::SequentialImpulseConstraintSolver.new
        collisionWorld = Gdx::DiscreteDynamicsWorld.new(dispatcher, broadphase, solver, collisionConfiguration)
        collisionWorld.setGravity(gravity)
        @world = collisionWorld
      end
    end
  end
end
