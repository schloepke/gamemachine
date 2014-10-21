require 'spec_helper'
require 'benchmark'
require "net/http"
require "uri"
require 'jruby/core_ext'
require 'digest'
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

    let(:player_id) {'player'}
    let(:id) {'one'}

    let(:test_object) do
      message = MessageLib::TestObject.new
      message.set_id(id)
      message.set_required_string('testing')
      message.set_fvalue(1.9)
      message.set_bvalue(true)
      message.set_dvalue(3.4)
      message.set_numbers64(555)
    end

    it "object store stress" do

      count = 0
      threads = []
      4.times do
        threads << Thread.new do
          keys = {}
          tobj = test_object.clone
          puts Benchmark.realtime {
            50.times do
              10000.times do |i|
                if keys[i]
                  id = keys[i]
                else
                  id = Digest::MD5.hexdigest(rand(10000).to_s + i.to_s)
                  keys[i] = id
                end
                tobj.set_id(id)
                MessageLib::TestObject.store.set(id,tobj)
                MessageLib::TestObject.store.get(id,3)
                sleep 0.001
              end
            end
          }
        end
      end
      threads.map(&:join)
      sleep 60
    end

    xit "cache test" do
      MessageLib::TestObject.cacheInit(100,200000)
      count = 100000

      cache = MessageLib::TestObject.get_cache
      cache.set(test_object.id,test_object)

      count.times do |i|
        obj = test_object.clone
        obj.set_id(i.to_s)
        cache.set(obj.id,obj)
      end

      puts Benchmark.realtime {
      count.times do |i|
        if obj = cache.get(i.to_s)
          obj.cache_increment_field("numbers64",1,10)
        end
      end
    }

      #count.times do |i|
      #   entity.set_id(i.to_s)
      #  MessageLib::Entity.store_delete('testing',entity.id)
      #end

    end

    xit "authorizes with cloudclient" do
      url = "http://localhost:9000/client/login/test"
      uri = URI.parse(url)

      response = Net::HTTP.post_form(uri, {'username' => 'chris', 'password' => 'pass'})
      puts response.inspect
      puts response.body

    end

    xit "serialized to json" do
      entity = MessageLib::Entity.new.set_id('player')
      puts entity.to_json
    end

    xit "couch http client" do
      entity = MessageLib::Entity.new.set_id('3')
      client = JavaLib::Couchclient.get_instance
      10.times do |i|
        id = "TESTING#{i}"
        response = client.put(id,entity.to_byte_array)
        puts response.status
        #puts response.body

        response = client.get(id)
        puts response.status
        if response.status == 200
          puts MessageLib::Entity.parse_from(response.body)
        end
        

        response = client.delete(id)
        puts response.status
        #puts response.body
      end
    end

    

    xit "finds model" do
      GameMachine::Application.orm_connect
      MessageLib::TestObject.delete_async('player1')
      if message = MessageLib::TestObject.find_by_player_id("player1")
        message.set_fvalue(1.9)
        message.save_async('player1')
      else
        message = MessageLib::TestObject.new
        message.set_required_string('testing')
        message.set_fvalue(1.9)
        message.set_bvalue(true)
        message.set_dvalue(3.4)
        message.set_numbers64(555)
        message.save_async('player1')
      end
      
      sleep 2
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
