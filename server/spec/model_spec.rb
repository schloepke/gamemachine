require 'spec_helper'
require 'json'
require 'virtus'
require 'benchmark'

module GameMachine
  describe "model" do

    class ScopedModel < GameMachine::Model
      attribute :health, Integer
      attribute :name, String
      attribute :defense_skill, Integer
      attribute :attack_skill, Integer
      set_id_scope :tm
    end

    class TestModel < GameMachine::Model
      attribute :health, Integer
      attribute :name, String
      attribute :defense_skill, Integer
      attribute :attack_skill, Integer
      validates :id, exclusion: { in: %w(one user) }
      validates :id, presence: true
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

    describe "#valid?" do
      it "should give correct answer" do
        test_model.id = 'one'
        expect(test_model.valid?).to be_false
        expect(test_model.errors.size).to eq 1
        test_model.id = "myname"
        expect(test_model.valid?).to be_true
      end
    end

    describe "#save" do

      it "should return true on success" do
        expect(test_model.save).to be_true
      end

      it "should return false if id is nil" do
        test_model.id = nil
        expect(test_model.save).to be_false
      end
    end

    describe "#save!" do
      it "should raise an exception if id is nil" do
        test_model.id = nil
        expect {test_model.save!}.to raise_error
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
