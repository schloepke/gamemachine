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

    it "define new method" do
      Entity.new.test
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
