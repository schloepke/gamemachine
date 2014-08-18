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

    let(:entity) {MessageLib::Entity.new.set_id(player_id)}

    subject do
      MessageLib::TestObject
    end

    describe "objectdb persistence" do
      
      describe "#dbDelete" do
        it "sends delete request to object store" do
          MessageLib::Entity.db_delete(player_id)
        end
      end

      describe "#dbPut" do
        it "sends save request to object store" do
          entity.db_put
        end
      end

      describe "#dbGet" do
        it "retrieves entity from the object store" do
          entity = MessageLib::Entity.db_get(player_id,1)
        end
      end
    end

    describe "Orm persistence" do
    	before(:each) do
    	 GameMachine::Application.orm_connect
       ModelLib::TestObject.open
       ModelLib::TestObject.delete_all
       ModelLib::TestObject.close
    	end

    	describe "#ormSave" do
        it "should return true" do
          expect(test_object.orm_save(player_id)).to be_truthy
        end
    	end

      describe "#ormFind" do
        it "should return test object with correct values" do
          test_object.orm_save(player_id)
          obj = subject.orm_find(id,player_id)
          expect(obj.required_string).to eql(test_object.required_string)
          expect(obj.fvalue).to eql(test_object.fvalue)
          expect(obj.bvalue).to eql(test_object.bvalue)
          expect(obj.dvalue).to eql(test_object.dvalue)
          expect(obj.numbers64).to eql(test_object.numbers64)
        end
      end

      describe "#ormWhere" do
        it "should return list with one test object" do
          test_object.orm_save(player_id)
          list = subject.orm_where('test_object_dvalue = ?',3.4)
          expect(list.to_a.size).to eql(1)
        end
      end

      describe "#ormDelete" do
        it "should return false if no record" do
          expect(test_object.orm_delete(player_id)).to be_falsy
        end

        it "should return true" do
          test_object.orm_save(player_id)
          expect(test_object.orm_delete(player_id)).to be_truthy
        end
      end
    end

  end
end