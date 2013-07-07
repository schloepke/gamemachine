require 'spec_helper'

module GameMachine
  describe ClientObserver do

    let(:client_id) {'client'}
    let(:actor_ref) {double('ActorRef')}

    subject do
      ClientObserver
    end

    describe "#register_observers" do
      it "returns true" do
       expect(subject.register_observer(client_id,actor_ref)).to be_true
       subject.remove_observer(client_id)
      end
    end

    describe "#notify_observers" do
      it "sends a ClientDisconnect message to each observer" do
        subject.register_observer(client_id,actor_ref)
        expect(actor_ref).to receive(:tell).with(kind_of(ClientDisconnect))
        subject.notify_observers(client_id)
      end
    end

  end
end
