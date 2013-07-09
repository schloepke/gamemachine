require 'spec_helper'

module GameMachine
  describe PlayerRegistry do

    let(:client_id) {'client'}
    let(:player_id) {'player'}
    let(:actor_ref) {double('ActorRef')}

    let(:player_logout) {PlayerLogout.new.set_player_id(player_id)}

    let(:client_connection) do
      ClientConnection.new.set_id(client_id).set_gateway('gateway')
    end

    let(:client_disconnect) do
      ClientDisconnect.new.set_client_connection(
        client_connection
      )
    end

    let(:client_message) {Helpers::GameMessage.new(player_id).client_message}

    let(:player_register) do
      client_message.set_client_connection(client_connection)
      PlayerRegister.new.
        set_client_connection(client_message.client_connection).
        set_player_id(client_message.player.id).
        set_observer('/blah')
    end

    let(:player_registry) do
      props = JavaLib::Props.new(PlayerRegistry);
      ref = JavaLib::TestActorRef.create(Server.instance.actor_system, props, 'test_registry');
      ref.underlying_actor
    end

    subject do
      PlayerRegistry
    end

    before(:each) do
      subject.clear_player_ids
      subject.clear_client_connections
      subject.clear_observers
    end

    describe "#on_receive" do

      it "registers player" do
        message = player_register
        message.set_observer(nil)
        expect(subject).to receive(:register_player).
          with(message.player_id,message.client_connection)
        expect(subject).to_not receive(:register_observer).
          with(message.player_id,kind_of(ActorRef))
        player_registry.on_receive(message)
      end

      it "registers player and observer" do
        expect(subject).to receive(:register_player).
          with(player_register.player_id,player_register.client_connection)
        expect(subject).to receive(:register_observer)
        player_registry.on_receive(player_register)
      end

      it "disconnects the client" do
        expect(subject).to receive(:client_disconnect).
          with(client_disconnect.client_connection)
        player_registry.on_receive(client_disconnect)
      end

      it "logs out the player" do
        expect(subject).to receive(:player_logout).
          with(player_logout.player_id)
        player_registry.on_receive(player_logout)
      end
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
