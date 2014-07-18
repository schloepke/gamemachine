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

    xit "trackdata test" do
       neighbors = MessageLib::Neighbors.new
       trackdata = MessageLib::TrackData.new
       trackdata.x = 100.0
       trackdata.y = 100.0
       trackdata.z = 100.0
       200.times do
         neighbors.add_track_data(trackdata)
       end

       tracktest = MessageLib::TrackTest.new
       200.times do
         tracktest.add_x(100.0)
         tracktest.add_y(100.0)
         tracktest.add_z(100.0)
       end
       100000000.times do
         tracktest.to_byte_array
         #neighbors.to_byte_array
       end
    end

    xit "singleton manager" do
      ref = Akka.instance.actor_system.actor_selection('/user/region_manager_proxy')
      ref.tell('HELLO',nil)
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
