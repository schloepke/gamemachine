require 'spec_helper'
module GameMachine

  describe "Message cache" do

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

    let(:raw_cache) {MessageLib::TestObject.cache.get_cache}
    let(:cache) {MessageLib::TestObject.cache}

    subject do
      MessageLib::TestObject::TestObjectCache
    end

    describe "#cacheSetFromUpdate" do
      
      it "should set simple value field" do
        raw_cache.set(test_object.id,test_object)
        cache_update = JavaLib::CacheUpdate.new(subject.java_class, test_object.id, "blah", "requiredString", JavaLib::CacheUpdate::SET)
        subject.set_from_update(cache_update)
        obj = cache.get(test_object.id,1000)
        expect(obj.get_required_string).to eq("blah")
      end

      it "should set list values" do
        raw_cache.set(test_object.id,test_object)
        cache_update = JavaLib::CacheUpdate.new(
          subject.java_class,
          test_object.id,
          java.util.Arrays.asList([11,12].to_java('java.lang.Integer')),
          "numbers",
          JavaLib::CacheUpdate::SET
        )
        subject.set_from_update(cache_update)
        obj = cache.get(test_object.id,1000)
        expect(obj.get_numbers_list.to_a).to eq([11,12])
      end
    end

    describe "#setField" do

      it "should use tell if non blocking" do
        raw_cache.set(test_object.id,test_object)
        ref = cache.set_field(test_object.id,'requiredString','something')
        ref.send
      end

      it "should set the field value" do
        raw_cache.set(test_object.id,test_object)
        ref = cache.set_field(test_object.id,'requiredString','something')
        obj = ref.result(1000)
        expect(obj.get_required_string).to eq('something')
      end

       it "should set list values" do
        raw_cache.set(test_object.id,test_object)
        ref = cache.set_field(test_object.id, "numbers",java.util.Arrays.asList([0,1].to_java('java.lang.Integer')))
        obj = ref.result(1000)
        expect(obj.get_numbers_list.to_a).to eq([0,1])
      end
    end


    describe "#cacheIncrementField" do
      it "should increment the field value" do
        raw_cache.set(test_object.id,test_object)
        ref = cache.increment_field(test_object.id,"numbers64",5)
        obj = ref.result(1000)
        expect(obj.get_numbers64).to eq(560)
      end
    end

    describe "#cacheDecrementField" do
      it "should decrement the field value" do
        raw_cache.set(test_object.id,test_object)
        ref = cache.decrement_field(test_object.id,"numbers64",5)
        obj = ref.result(1000)
        expect(obj.get_numbers64).to eq(550)
      end
    end
  end

end