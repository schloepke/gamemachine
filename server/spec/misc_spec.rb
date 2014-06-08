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


    xit "vector3" do
      vector = MessageLib::Vector3.new.set_x(0.0)
      bytes = vector.to_byte_array
      File.open('/tmp/bytes.txt','wb') {|f| f.write(bytes)}
      vector = MessageLib::Vector3.parse_from(bytes)
      expect(vector.x).to eq 0
    end

    xit "parses game messages" do
      file = File.join(GameMachine.app_root,'config','game_messages.proto')
      obj = Protobuf::GameMessages.new(file)
      fields = obj.create_entity_fields
      puts fields.inspect
    end

    xit "extends protobuf messages" do
      MessageLib::Entity.class_eval do
        define_method :doit do
          "done it"
        end
      end
      entity = MessageLib::Entity.new.set_id('3')
      puts entity.doit
    end

    xit "protobuf delimited" do
      message = MessageLib::ClientMessage.new.add_entity(
        MessageLib::Entity.new.set_id('asjfl;asjflkasjdfljaslf')
      )
      prefixed_bytes = message.to_prefixed_byte_array 
      bytes = message.to_byte_array
      puts prefixed_bytes.size
      puts bytes.size
      stream = JavaLib::ByteArrayInputStream.new(prefixed_bytes)
      puts "stream byte count #{stream.available}"
      ProtoLib::CodedInput.readRawVarint32(stream, 1)
      puts "stream byte count #{stream.available}"
      bytes = Java::byte[stream.available].new
      stream.read(bytes,0,stream.available)
    end

    xit "marshal" do
      system = Akka.instance.actor_system
      serialization = JavaLib::SerializationExtension.get(system)
      serializer = serialization.findSerializerFor(entity)
      bytes = serializer.toBinary(entity)
      new_entity = serializer.fromBinary(bytes,entity.get_class)
    end
    
  end
end
