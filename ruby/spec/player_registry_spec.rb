require 'spec_helper'

module GameMachine
  describe PlayerRegistry do

    let(:client_id) {'client'}
    let(:player_id) {'player'}
    let(:actor_ref) {double('ActorRef')}

    let(:client_connection) do
      ClientConnection.new.set_id(client_id).set_gateway('gateway')
    end

    subject do
      PlayerRegistry
    end

    before(:each) do
      subject.clear_player_ids
      subject.clear_client_connections
      subject.clear_observers
    end

    describe "#register_player" do
      it "sets the correct values for player and client" do
       expect(subject.register_player(player_id,client_connection)).to be_true
       expect(subject.player_id_for(client_id)).to eq(player_id)
       expect(subject.client_id_for(player_id)).to eq(client_id)
      end
    end

    describe "#remove_client_connection" do
      it "removes the client entry" do
       subject.register_player(player_id,client_connection)
       subject.remove_client_connection(player_id)
       expect(subject.client_id_for(player_id)).to be_nil
      end
    end

    describe "#remove_player" do
      it "removes the player entry" do
       subject.register_player(player_id,client_connection)
       subject.remove_player_id(client_id)
       expect(subject.player_id_for(client_id)).to be_nil
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

      it "calls notify_observers with player id and client id" do
        subject.register_player(player_id,client_connection)
        expect(subject).to receive(:notify_observers).with(player_id,client_id)
        subject.client_disconnect(client_connection)
      end
      it "should not do anything if player is not registered" do
        expect(subject).to_not receive(:notify_observers)
        subject.client_disconnect(client_connection)
      end
    end

    describe "#player_logout" do

      it "calls notify_observers with player id and client id" do
        subject.register_player(player_id,client_connection)
        expect(subject).to receive(:notify_observers).with(player_id,client_id)
        subject.player_logout(player_id)
      end

      it "does not do anything if player not registered" do
        expect(subject).to_not receive(:notify_observers)
        subject.player_logout(player_id)

      end
    end

    describe "#notify_observers" do
      before(:each) do
        subject.register_player(player_id,client_connection)
        subject.register_observer(player_id,actor_ref)
      end

      it "sends Disconnected message to each observer" do
        expect(actor_ref).to receive(:tell).with kind_of(Disconnected)
        subject.notify_observers(player_id,client_id)
      end

      it "removes player, client, client connection, and observers" do
        actor_ref.stub(:tell)
        expect(subject).to receive(:remove_player_id).with(client_id)
        expect(subject).to receive(:remove_client_connection).with(player_id)
        expect(subject).to receive(:remove_observers).with(player_id)
        subject.notify_observers(player_id,client_id)
      end
    end

  end
end
