module GameMachine
  module Gdx
    include_package 'com.badlogic.gdx.physics.bullet'
    include_package 'com.badlogic.gdx.math'
    java_import('com.badlogic.gdx.physics.bullet.btDefaultCollisionConfiguration') {'DefaultCollisionConfiguration'}
    java_import('com.badlogic.gdx.physics.bullet.btCollisionDispatcher') {'CollisionDispatcher'}
    java_import('com.badlogic.gdx.physics.bullet.btDbvtBroadphase') {'DbvtBroadphase'}
    java_import('com.badlogic.gdx.physics.bullet.btSequentialImpulseConstraintSolver') {'SequentialImpulseConstraintSolver'}
    java_import('com.badlogic.gdx.physics.bullet.btDiscreteDynamicsWorld') {'DiscreteDynamicsWorld'}
    java_import('com.badlogic.gdx.physics.bullet.btCollisionObject') {'CollisionObject'}
    java_import('com.badlogic.gdx.physics.bullet.btCollisionShape') {'CollisionShape'}
    java_import('com.badlogic.gdx.physics.bullet.btMotionState') {'MotionState'}
    java_import('com.badlogic.gdx.physics.bullet.btRigidBody') {'RigidBody'}
    java_import('com.badlogic.gdx.physics.bullet.btRigidBodyConstructionInfo') {'RigidBodyConstructionInfo'}
    java_import('com.badlogic.gdx.physics.bullet.btBoxShape') {'BoxShape'}
  end
end
