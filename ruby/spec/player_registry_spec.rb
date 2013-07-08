require 'spec_helper'

module GameMachine
  describe PlayerRegistry do

    let(:client_id) {'client'}
    let(:player_id) {'player'}
    let(:actor_ref) {double('ActorRef')}

    subject do
      PlayerRegistry
    end

    before(:each) do
      subject.clear_players
      subject.clear_clients
      subject.clear_observers
    end

    describe "#register_player" do
      it "sets the correct values for player and client" do
       expect(subject.register_player(player_id,client_id)).to be_true
       expect(subject.player(client_id)).to eq(player_id)
       expect(subject.client(player_id)).to eq(client_id)
      end
    end

    describe "#remove_client" do
      it "removes the client entry" do
       subject.register_player(player_id,client_id)
       subject.remove_client(player_id)
       expect(subject.client(player_id)).to be_nil
      end
    end

    describe "#remove_player" do
      it "removes the player entry" do
       subject.register_player(player_id,client_id)
       subject.remove_player(client_id)
       expect(subject.player(client_id)).to be_nil
      end
    end

    describe "#register_observer" do
      it "adds observer and returns true" do
        expect(subject.register_observer(player_id,actor_ref)).to be_true
        expect(subject.observers_for(player_id)).to eq([actor_ref])
      end
    end

    describe "#remove_observers" do
      it "removes observers for player id" do
        subject.register_observer(player_id,actor_ref)
        subject.remove_observers(player_id)
        expect(subject.observers_for(player_id)).to eq([])
      end
    end

    describe "#observers_for" do
      it "returns observers for player id" do
        subject.register_observer(player_id,actor_ref)
        expect(subject.observers_for(player_id)).to eq([actor_ref])
        subject.register_observer(player_id,actor_ref)
        expect(subject.observers_for(player_id)).to eq([actor_ref,actor_ref])
      end
    end

    describe "#client_disconnect" do
      before(:each) do
        subject.register_player(player_id,client_id)
      end

      it "calls notify_observers with player id and client id" do
        expect(subject).to receive(:notify_observers).with(player_id,client_id)
        subject.client_disconnect(client_id)
      end
    end

    describe "#player_logout" do
      before(:each) do
        subject.register_player(player_id,client_id)
      end

      it "calls notify_observers with player id and client id" do
        expect(subject).to receive(:notify_observers).with(player_id,client_id)
        subject.player_logout(player_id)
      end
    end

    describe "#notify_observers" do
      before(:each) do
        subject.register_player(player_id,client_id)
        subject.register_observer(player_id,actor_ref)
      end

      it "sends Disconnected message to each observer" do
        expect(actor_ref).to receive(:tell).with kind_of(Disconnected)
        subject.notify_observers(player_id,client_id)
      end

      it "removes player, client, and observers" do
        actor_ref.stub(:tell)
        expect(subject).to receive(:remove_player).with(client_id)
        expect(subject).to receive(:remove_client).with(player_id)
        expect(subject).to receive(:remove_observers).with(player_id)
        subject.notify_observers(player_id,client_id)
      end
    end

  end
end
