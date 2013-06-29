require 'spec_helper'

module GameMachine
  describe "misc" do 
    let(:entity) do 
      entity = Entity.new
      entity.set_id('1')
      player = Player.new
      player.set_authtoken('authorized')
      player.set_id('2')
      entity.set_player(player)
      entity
    end

    it "msgpacks" do
      e = GameEntity.new
      e.one = GameEntity.new(
        :one => 'adfasdfas',
        :two => GameEntity.new(:one => ComponentOne.new, :three => 3)
      )
      e.two = ComponentOne.new(:one => 123)
      puts e.kind_of?(Component)
      puts e.is_a?(Component)
      h = e.to_hash
      puts h.inspect
      puts Benchmark.realtime {50000.times {e.to_hash}}
      e = GameEntity.from_hash(h)
      packed = e.pack
      puts packed.inspect
      unpacked = GameEntity.unpack(packed)
      puts Benchmark.realtime {50.times {e.pack}}
    end

    it "marshal" do
      system = Server.instance.actor_system
      serialization = JavaLib::SerializationExtension.get(system)
      serializer = serialization.findSerializerFor(entity)
      bytes = serializer.toBinary(entity)
      new_entity = serializer.fromBinary(bytes,entity.get_class)
    end
    
    it "deadletter" do
      #ref = LocalEcho.find_distributed('blah')
      #ref.send_message('testa')
      #sleep 2
    end
  end
end
