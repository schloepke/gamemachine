require 'spec_helper'

module GameMachine
  module GameSystems
    describe SingletonManager do

      subject do
        props = JavaLib::Props.new(SingletonManager);
        ref = JavaLib::TestActorRef.create(Akka.instance.actor_system, props, 'singleton_manager_test');
        ref.underlying_actor
      end

      let(:create_singleton) do
        MessageLib::Entity.new.set_create_singleton(
          MessageLib::CreateSingleton.new.set_id('npc1').
          set_controller('GameMachine::GameSystems::SingletonController')
        ).set_id('entity')
      end

      let(:notify_singleton) do
        MessageLib::Entity.new.set_notify_singleton(
          MessageLib::NotifySingleton.new.set_id('npc1')
        ).set_id('entity')
      end

      let(:destroy_singleton) do
        MessageLib::Entity.new.set_destroy_singleton(
          MessageLib::DestroySingleton.new.set_id('npc1')
        ).set_id('entity')
      end

      let(:actor_ref) {double('Actor::Ref', :tell => true)}
      let(:actor_refs) {[actor_ref]}

      describe "#on_receive" do

        before(:each) do
          subject.stub(:schedule_update)
          subject.stub(:create_singleton_routers).and_return(actor_refs)
          SingletonRouter.stub(:find_distributed_local).and_return(actor_ref)
        end

        context "receives a CreateSingleton message" do
          it "sends CreateSingleton message to router" do
            expect(actor_ref).to receive(:tell).with(create_singleton)
            subject.on_receive(create_singleton)
          end
        end

        context "receives a NotifySingleton message" do
          it "sends NotifySingleton message to router" do
            expect(actor_ref).to receive(:tell).with(notify_singleton)
            subject.on_receive(notify_singleton)
          end
        end

        context "receives a DestroySingleton message" do
          it "sends DestroySingleton message to router" do
            expect(actor_ref).to receive(:tell).with(destroy_singleton)
            subject.on_receive(destroy_singleton)
          end
        end

        context "receives an update message" do
          it "sends an update message to router" do
            subject.post_init
            expect(actor_refs.first).to receive(:tell)
            subject.on_receive('update')
          end
        end

      end
    end
  end
end

