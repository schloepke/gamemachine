require 'spec_helper'
require 'json'
require 'benchmark'

module GameMachine
  describe "model" do


    class TestModel < GameMachine::Model
      attribute :health, Integer
      attribute :name, String
      attribute :defense_skill, Integer
      attribute :attack_skill, Integer
    end

    class ScopedModel < GameMachine::Model
      attribute :health, Integer
      attribute :name, String
      attribute :defense_skill, Integer
      attribute :attack_skill, Integer
      attribute :test_models, Array
      attribute :test_model, TestModel
      set_id_scope :tm
    end


    let(:scoped_model) do
      model = ScopedModel.new(:health => 100, :id => 'myid')
    end

    let(:scoped_storage_entity) do
      scoped_model.to_storage_entity
    end

    let(:test_model) do
      model = TestModel.new(:health => 100, :id => 'myid')
    end



    # Unfinished but leaving here for now
    describe "nesting" do
      it "should parse correctly" do
          scoped_model.test_models = []
          scoped_model.test_models << test_model
          scoped_model.test_models << test_model
          scoped_model.test_model = test_model
          hash = JSON.parse(scoped_model.to_json)
          ScopedModel.from_hash(hash)
      end
    end

    context :scoped_model do

      describe "#unscoped_id" do
        it "should return unscoped id" do
          scoped_model.id = 'tm|myid'
          expect(scoped_model.unscoped_id).to eq 'myid'
        end
      end

      describe "#scoped_id" do
        it "should return scoped id" do
          expect(scoped_model.scoped_id).to eq 'tm|myid'
        end
      end

      describe "#from_entity" do
        it "should unscope id" do
          model = ScopedModel.from_entity(scoped_storage_entity,:json_storage)
          expect(model.id).to eq 'myid'
        end
      end
    end


    describe "#save" do

      it "should return true on success" do
        expect(test_model.save).to be_true
      end
    end

    describe "#save!" do
      it "should call datastore.put!" do
        expect_any_instance_of(GameMachine::Commands::DatastoreCommands).to receive(:put!)
        test_model.save!
      end

      it "should not raise an exception if id is not nil" do
        expect {test_model.save!}.to_not raise_error
      end
    end

    describe "#to_json" do
      it "should set klass" do
        model_hash = JSON.parse(test_model.to_json)
        expect(model_hash.has_key?('klass')).to be_true
      end
    end

    describe "#to_entity" do
      it "should return an entity with json entity" do
        expect(test_model.to_entity.has_json_entity).to be_true
      end
    end

    describe "#from_entity" do
      it "should return a test model" do
        entity = test_model.to_entity
        expect(TestModel.from_entity(entity)).to be_a(TestModel)
      end
    end
  end
end
