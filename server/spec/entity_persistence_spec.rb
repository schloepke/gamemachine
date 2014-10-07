require 'spec_helper'
module GameMachine

  describe "entity persistence" do

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

    let(:entity) {MessageLib::Entity.new.set_id(id)}

    subject do
      MessageLib::TestObject
    end

    describe "object cache" do

      before(:each) do
        [1,2,3,4,5,6,7,8,9,10].each do |i|
          #test_object.add_numbers(i)
        end
        #MessageLib::TestObject.cacheInit(100,100)
      end

      describe "#cacheSetFromUpdate" do
        
        it "should set simple value field" do
          cache = MessageLib::TestObject.get_cache
          cache.set(test_object.id,test_object)
          cache_update = JavaLib::CacheUpdate.new(MessageLib::TestObject.java_class, test_object.id, "blah", "requiredString", JavaLib::CacheUpdate::SET)
          MessageLib::TestObject.cache_set_from_update(cache_update)
          obj = cache.get(test_object.id)
          expect(obj.get_required_string).to eq("blah")
        end

        it "should set list values" do
          cache = MessageLib::TestObject.get_cache
          cache.set(test_object.id,test_object)
          cache_update = JavaLib::CacheUpdate.new(
            MessageLib::TestObject.java_class,
            test_object.id,
            java.util.Arrays.asList([11,12].to_java('java.lang.Integer')),
            "numbers",
            JavaLib::CacheUpdate::SET
          )
          MessageLib::TestObject.cache_set_from_update(cache_update)
          obj = cache.get(test_object.id)
          expect(obj.get_numbers_list.to_a).to eq([11,12])
        end
      end

      describe "#cacheSet" do
        it "should set the field value" do
          cache = MessageLib::TestObject.get_cache
          cache.set(test_object.id,test_object)
          test_object.set_required_string('something')
          obj = test_object.cache_set(1000)
          expect(obj.get_required_string).to eq('something')
        end
      end

      describe "#cacheSetField" do
        it "should set the field value" do
          cache = MessageLib::TestObject.get_cache
          cache.set(test_object.id,test_object)
          obj = test_object.cache_set_field("numbers",java.util.Arrays.asList([0,1].to_java('java.lang.Integer')),1000)
          expect(obj.get_numbers_list.to_a).to eq([0,1])
        end
      end

      describe "#cacheIncrementField" do
        it "should increment the field value" do
          cache = MessageLib::TestObject.get_cache
          cache.set(test_object.id,test_object)
          obj = test_object.cache_increment_field("numbers64",5,1000)
          expect(obj.get_numbers64).to eq(560)
        end
      end

      describe "#cacheDecrementField" do
        it "should decrement the field value" do
          cache = MessageLib::TestObject.get_cache
          cache.set(test_object.id,test_object)
          obj = test_object.cache_decrement_field("numbers64",5,1000)
          expect(obj.get_numbers64).to eq(550)
        end
      end
    end

    describe "objectdb persistence" do
      
      describe "#query", :if => GameMachine::Application.config.store == 'gamecloud' do
        it "performs a query on the gamecloud" do
          GameMachine::DataStore.instance.delete_matching("players",'')
          player = MessageLib::Player.new.set_id('player2').set_password_hash('blah')
          GameMachine::DataStore.instance.set("players##player2",player)
          messages = GameMachine::DataStore.instance.query("players",'',200,'Player')
          expect(messages.size).to eq(1)
          expect(messages.first.id).to eq('player2')
        end

        it "should return players with matching ids" do
          100.times do |i|
            player = MessageLib::Player.new.set_id("player#{i}").set_password_hash('blah')
            GameMachine::DataStore.instance.set("players##player#{i}",player)
          end
          messages = GameMachine::DataStore.instance.query("players",'',200,'Player')
          expect(messages.size).to eq(100)

          messages2 = GameMachine::DataStore.instance.query("players",'player95',200,'Player')
          expect(messages2.size).to eq(1)
        end
      end

      describe "#dbDelete" do
        it "sends delete request to object store" do
          MessageLib::Entity.store_delete(player_id,id)
        end
      end

      describe "#dbPut" do
        it "sends save request to object store" do
          entity.store_set(player_id)
        end
      end

      describe "#dbGet" do
        it "retrieves entity from the object store" do
          entity.store_set(player_id)
          sleep 1
          entity = MessageLib::Entity.store_get(player_id,id,6000)
          expect(entity.id).to eql id
        end
      end

      describe "store any message that has id" do
        it "stores and retrieves message having correct id" do
          player = MessageLib::Player.new.set_id('player2').set_password_hash('blah')
          player.store_set(player_id)
          sleep 1
          player = MessageLib::Player.store_get(player_id,'player2',2000)
          expect(player.id).to eql('player2')
        end
      end
    end

    describe "Orm persistence", :if => GameMachine::Application.config.orm do
    	before(:each) do
    	 GameMachine::Application.orm_connect
       ModelLib::TestObject.open
       ModelLib::TestObject.delete_all
       ModelLib::TestObject.close
    	end

    	describe "#ormSave" do
        it "should return true" do
          expect(test_object.db_save(player_id)).to be_truthy
        end
    	end

      describe "#ormFind" do
        it "should return test object with correct values" do
          test_object.db_save(player_id)
          obj = subject.db_find(id,player_id)
          expect(obj.required_string).to eql(test_object.required_string)
          expect(obj.fvalue).to eql(test_object.fvalue)
          expect(obj.bvalue).to eql(test_object.bvalue)
          expect(obj.dvalue).to eql(test_object.dvalue)
          expect(obj.numbers64).to eql(test_object.numbers64)
        end
      end

      describe "#ormWhere" do
        it "should return list with one test object" do
          test_object.db_save(player_id)
          list = subject.db_where('test_object_dvalue = ?',3.4)
          expect(list.to_a.size).to eql(1)
        end
      end

      describe "#ormDelete" do
        it "should return false if no record" do
          expect(test_object.db_delete(player_id)).to be_falsy
        end

        it "should return true" do
          test_object.db_save(player_id)
          expect(test_object.db_delete(player_id)).to be_truthy
        end
      end
    end

  end
end