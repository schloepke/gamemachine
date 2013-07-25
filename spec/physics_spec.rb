require 'spec_helper'

def move_player(id,x,y)
  player = Player.new.set_id(id.to_s)
  move = PlayerMove.new.set_target(
    Target.new.set_x(x).set_y(y)
  )
  Entity.new.set_player(player).set_player_move(move)
end
module GameMachine
def test
      Gdx::Bullet.init
      world = Physics::World3d.new
      box = Gdx::BoxShape.new(Gdx::Vector3.new(0,0,0))
      info = Gdx::RigidBodyConstructionInfo.new(0.0, nil, box, Gdx::Vector3::Zero)
      body = Gdx::RigidBody.new(info)
      world.world.add_rigid_body(body)
      body.translate(Gdx::Vector3.new(5,5,5))

end
end

module GameMachine
  describe "physics" do

    pending do
    it "gdx" do
      s = Gdx::Vector3.new(0,0,0)
      e = Gdx::Vector3.new(3000,2000,900)
      puts Benchmark.realtime {1000.times {s.dst(e)}}
    end

    it "simulation" do
      0.times do |i|
        entity = move_player("player_#{i}",rand(100).floor,rand(100).floor)
        Physics::World.find.tell(entity)
      end
    end

    it "works" do
      body = GameMachine::Physics::Body.new
      world = Physics::World.create_world(1000,1000)
      world.add_body(body.body)
      puts "mass=#{body.mass}"
      body.move_to(50,4,50)
      0.times do
        puts "velocity=#{body.velocity}"
        puts "#{body.x} #{body.y}"
        world.update(1)
      end
    end
    end
  end
end
