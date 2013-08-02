require 'spec_helper'
module GameMachine
  describe "misc" do 
    let(:entity) do 
      entity = Entity.new
      entity.id = '1'
      player = Player.new
      player.authtoken = 'authorized'
      player.id = '2'
      entity.player = player
      entity
    end

    it "sends protocol buffer to .net actor" do
      MonoTest.init_mono
    end

    it "marshal" do
      system = Akka.instance.actor_system
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
