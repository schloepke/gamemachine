require 'spec_helper'

module GameMachine
  module GameSystems
    describe SingletonRouter do

      subject do
        props = JavaLib::Props.new(SingletonRouter);
        ref = JavaLib::TestActorRef.create(Akka.instance.actor_system, props, 'singleton_router_test');
        ref.underlying_actor.post_init
        ref.underlying_actor
      end

      let(:create_singleton) do
        MessageLib::Entity.new.set_create_singleton(
          MessageLib::CreateSingleton.new.set_id('singleton1').
          set_controller('GameMachine::GameSystems::SingletonController')
        ).set_id('entity')
      end


      let(:notify_singleton) do
        MessageLib::Entity.new.set_notify_singleton(
          MessageLib::NotifySingleton.new.set_id('singleton1')
        ).set_id('entity')
      end

      let(:destroy_singleton) do
        MessageLib::Entity.new.set_destroy_singleton(
          MessageLib::DestroySingleton.new.set_id('singleton1')
        ).set_id('entity')
      end

      let(:actor_ref) {double('Actor::Ref', :tell => true)}

      describe "#on_receive" do

        context "receives a CreateSingleton message" do
          it "creates an singleton controller actor" do
            expect(subject).to receive(:create_child_controller).with(create_singleton)
            subject.on_receive(create_singleton)
          end
        end

        context "receives a CreateSingleton message with nonexistant controller class" do
          let(:create_bad_singleton) do
            MessageLib::Entity.new.set_create_singleton(
              MessageLib::CreateSingleton.new.set_id('singleton1').
              set_controller('BadController')
            ).set_id('entity')
          end

          it "logs an error" do
            expect(GameMachine.logger).to receive(:error).
              with("CreateSingleton error: NameError uninitialized constant BadController")
            subject.on_receive(create_bad_singleton)
          end
        end

        context "receives a NotifySingleton message" do
          it "calls on_receive on the singleton controller with message" do
            subject.on_receive(create_singleton)
            expect_any_instance_of(SingletonController).to receive(:on_receive).with(notify_singleton)
            subject.on_receive(notify_singleton)
          end
        end

        context "receives a NotifySingleton message after being destroyed" do
          it "logs an error message" do
            subject.on_receive(create_singleton)
            subject.on_receive(destroy_singleton)
            expect(GameMachine.logger).to receive(:error).
              with("Singleton Controller for singleton1 not found")
            subject.on_receive(notify_singleton)
          end
        end

        context "receives a DestroySingleton message" do
          it "calls destroy on the singleton controller with message" do
            subject.on_receive(create_singleton)
            expect(subject).to receive(:destroy_child_controller).
              with(destroy_singleton)
            subject.on_receive(destroy_singleton)
          end
        end

        context "receives an update message" do
          it "calls update on the singleton controller" do
            subject.on_receive(create_singleton)
            expect_any_instance_of(SingletonController).to receive(:on_receive).
              with('update')
            subject.on_receive('update')
          end
        end
      end
    end
  end
end
