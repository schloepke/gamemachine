require 'spec_helper'

module GameMachine
  describe "misc" do 
    let(:entity) do
      entity = Entity.new
      entity.set_id('1')
      player_connection = PlayerConnection.new
      player_connection.set_id('client1')
      player_connection.set_player(Player.new.set_id('1'))
      entity.set_player_connection(player_connection)
      entity
    end

    it "marshal" do
      system = GameMachineLoader.get_actor_system
      serialization = SerializationExtension.get(system)
      serializer = serialization.findSerializerFor(entity)
      bytes = serializer.toBinary(entity)
      new_entity = serializer.fromBinary(bytes,entity.get_class)
    end
    
    it "deadletter" do
      ref = LocalEcho.find_distributed('blah')
      ref.send_message('testa')
      sleep 2
    end
  end
end
