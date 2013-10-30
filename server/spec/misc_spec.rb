require 'spec_helper'
require 'jruby/core_ext'
module GameMachine
  describe "misc" do 
    let(:entity) do 
      entity = MessageLib::Entity.new
      entity.id = '1'
      player = MessageLib::Player.new
      player.authtoken = 'authorized'
      player.id = '2'
      entity.player = player
      entity
    end

    it "calls java" do
      cls = GameMachine::Commands::Proxy.become_java! false
      puts GameMachine::Commands::Proxy.java_class
      GameMachine::Commands::Proxy.call_java
    end

    it "marshal" do
      system = Akka.instance.actor_system
      serialization = JavaLib::SerializationExtension.get(system)
      serializer = serialization.findSerializerFor(entity)
      bytes = serializer.toBinary(entity)
      new_entity = serializer.fromBinary(bytes,entity.get_class)
    end
    
  end
end
