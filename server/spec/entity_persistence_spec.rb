require 'spec_helper'
java.lang.System.setProperty("activejdbc.log","")
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

    describe "objectdb persistence" do
      
      it "orm" do
        expect(GameMachine::Application.config.orm).to be_truthy
      end

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
          10.times do |i|
            player = MessageLib::Player.new.set_id("player#{i}").set_password_hash('blah')
            GameMachine::DataStore.instance.set("players##player#{i}",player)
          end
          messages = GameMachine::DataStore.instance.query("players",'',200,'Player')
          expect(messages.size).to eq(100)

          messages2 = GameMachine::DataStore.instance.query("players",'player95',200,'Player')
          expect(messages2.size).to eq(1)
        end
      end

      describe "#delete" do
        it "sends delete request to object store" do
          MessageLib::Entity.store.delete(player_id,id)
        end
      end

      describe "#set" do
        it "sends save request to object store" do
          MessageLib::Entity.store.set(player_id,entity)
        end
      end

      describe "#get" do
        it "retrieves entity from the object store" do
          MessageLib::Entity.store.set(player_id,entity)
          sleep 1
          entity = MessageLib::Entity.store.get(player_id,id,6000)
          expect(entity.id).to eql id
        end
      end

      describe "store any message that has id" do
        it "stores and retrieves message having correct id" do
          player = MessageLib::Player.new.set_id('player2').set_password_hash('blah')
          MessageLib::Player.store.set(player_id,player)
          sleep 1
          player = MessageLib::Player.store.get(player_id,'player2',2000)
          expect(player.id).to eql('player2')
        end
      end
    end

    describe "Orm persistence" do
    	before(:each) do
        subject.db.stop
        subject.db.start
      	GameMachine::Application.orm_connect
        ModelLib::TestObject.open
        ModelLib::TestObject.delete_all
        ModelLib::TestObject.close
    	end

    	describe "#save" do
        it "should return true" do
          expect(MessageLib::TestObject.db.save(test_object)).to be_truthy
        end
    	end

      describe "#save_async" do
        it "should return true" do
          expect(MessageLib::TestObject.db.save_async(test_object)).to be_nil
        end
      end

      describe "#find" do
        it "should return test object with correct values" do
          subject.db.save(test_object)
          obj = subject.db.find(id)
          expect(obj.required_string).to eql(test_object.required_string)
          expect(obj.fvalue).to eql(test_object.fvalue)
          expect(obj.bvalue).to eql(test_object.bvalue)
          expect(obj.dvalue).to eql(test_object.dvalue)
          expect(obj.numbers64).to eql(test_object.numbers64)
        end
      end

      describe "#where" do
        it "should return list with one test object" do
          subject.db.save(test_object)
          list = subject.db.where('test_object_dvalue = ?',3.4)
          expect(list.to_a.size).to eql(1)
        end
      end

      describe "#delete" do
        it "should return false if no record" do
          expect(subject.db.delete(test_object.id)).to be_falsy
        end

        it "should return true" do
          subject.db.save(test_object)
          expect(subject.db.delete(test_object.id)).to be_truthy
        end
      end

      describe "#delete_async" do
        it "should delete record" do
          subject.db.save(test_object)
          expect(subject.db.delete_async(test_object.id)).to be_nil
          sleep 1
          obj = subject.db.find(test_object.id)
          expect(obj).to be_nil
        end
      end
    end

  end
end