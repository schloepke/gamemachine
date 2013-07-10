require 'spec_helper'

module GameMachine
  describe ActorBuilder do

    let(:actor_class) {GameSystems::LocalEcho}

    let(:router) {double('Router')}

    subject do
     ActorBuilder.new(actor_class)
    end

    describe "#with_name" do
      it "should set name and return self" do
        expect(subject.with_name('blah')).to eq(subject)
        expect(subject.name).to eq('blah')
      end
    end

    describe "#with_parent" do
      it "returns self" do
        expect(subject.with_parent('blah')).to eq(subject)
      end
    end

    describe "#with_router" do
      it "calls new on router with num_routers and return self" do
        expect(router).to receive(:new).with(10)
        expect(subject.with_router(router,10)).to eq(subject)
      end
    end

    describe "#distributed" do
      it "returns self" do
        expect(subject.distributed(1)).to eq(subject)
      end
    end

    describe "#start" do
      it "creates the actor and returns the actor ref" do
        expect(subject.with_name('blah').start).to be_kind_of((JavaLib::ActorRef))
      end

      it "adds a hashring if distributed" do
        expect(GameSystems::LocalEcho).to receive(:add_hashring).with(
          GameSystems::LocalEcho.name,kind_of(Hashring)
        )
        subject.distributed(1).start
      end
    end

  end
end


