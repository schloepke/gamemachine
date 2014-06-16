
require 'spec_helper'
module GameMachine
  describe ClientManager do

    let(:player_id) {'player_test'}
    let(:actor_name) {'testactor'}
    let(:client_name) {'testclient'}
    let(:actor_ref) {double('Actor::Ref')}
    let(:player) {MessageLib::Player.new.set_id(player_id)}

    let("actor_register") do
      MessageLib::Entity.new.set_id(client_name).set_client_manager_register(
        MessageLib::ClientManagerRegister.new.set_register_type('actor').
          set_name(actor_name)
      )
    end

    let("actor_unregister") do
      MessageLib::Entity.new.set_id(client_name).set_client_manager_unregister(
        MessageLib::ClientManagerUnregister.new.set_register_type('actor').
          set_name(actor_name)
      )
    end

    let("client_register") do
      MessageLib::Entity.new.set_id(client_name).set_client_manager_register(
        MessageLib::ClientManagerRegister.new.set_register_type('client').
          set_name(client_name)
      ).set_player(player).set_client_connection(client_connection)
    end

    let("client_unregister") do
      MessageLib::Entity.new.set_id(client_name).set_client_manager_unregister(
        MessageLib::ClientManagerUnregister.new.set_register_type('client').
          set_name(client_name)
      ).set_player(player).set_client_connection(client_connection)
    end

    let('client_disconnected_event') do
      MessageLib::Entity.new.set_id(client_name).set_client_event(
        MessageLib::ClientEvent.new.set_event('disconnected').set_client_id(client_name).
        set_sender_id(sender_id).set_player_id(player_id)
      )
    end

    let(:sender_id) {['server2',ClientManager.name].join('|')}

    let('client_connected_event') do
      MessageLib::Entity.new.set_id(client_name).set_client_event(
        MessageLib::ClientEvent.new.set_event('connected').set_client_id(client_name).
        set_sender_id(sender_id).set_player_id(player_id)
      )
    end

    let(:client_connection) do
      MessageLib::ClientConnection.new.set_id(client_name).set_gateway('udp').
        set_server('server')
    end

    let('local_player_message') do
      MessageLib::Entity.new.set_id(client_name).set_player(player).
        set_send_to_player(true)
    end

    let('remote_player_message') do
      MessageLib::Entity.new.set_id(client_name).set_player(player).
        set_send_to_player(true)
    end

    subject do
      ref = Actor::Builder.new(ClientManager).with_name('client_manager_test').test_ref
      ref.underlying_actor
    end

    before(:each) do
      subject.class.local_players.delete(player_id)
    end

    describe "#on_receive" do

      it "should not process client even if message is from self" do
        client_connected_event.client_event.set_sender_id("default|me")
        expect(subject).to_not receive(:process_client_event)
        subject.on_receive(client_connected_event)
      end
    end

    describe "#send_to_player" do
      it "should send message directly to player if player is local" do
        subject.local_clients[client_name] = client_connection
        subject.players[player_id] = client_name
        subject.class.local_players[player_id] = true
        actor_ref.stub(:tell)
        expect(Actor::Base).to receive(:find).with(player_id).and_return(actor_ref)
        ClientManager.send_to_player(local_player_message)
      end

      it "should send message to remote manager if player is remote" do
        subject.remote_clients[client_name] = actor_ref
        subject.players[player_id] = client_name
        ClientManager.stub(:find).and_return(actor_ref)
        expect(actor_ref).to receive(:tell).with(remote_player_message)
        ClientManager.send_to_player(remote_player_message)
      end
    end

    describe "#process_client_event" do
      it "on connect should set players entry" do
        subject.on_receive(client_connected_event)
        expect(subject.players.has_key?(player_id)).to be_true
      end

      it "on connect should set remote_clients entry" do
        subject.on_receive(client_connected_event)
        expect(subject.remote_clients.has_key?(client_name)).to be_true
      end

      it "on disconnect event should remove remote client reference" do
        subject.remote_clients[client_name] = true
        subject.on_receive(client_disconnected_event)
        expect(subject.remote_clients.has_key?(client_name)).to be_false
      end
    end

    describe "#register_sender" do
      it "should add local client entry if client" do
        subject.on_receive(client_register)
        expect(subject.local_clients.has_key?(client_name)).to be_true
      end

      it "should add local actor entry if actor" do
        subject.on_receive(actor_register)
        expect(subject.local_actors.has_key?(actor_name)).to be_true
      end

      it "client register should call send_client_event" do
        expect(subject).to receive(:send_client_event).with(client_name,player_id,'connected')
        subject.on_receive(client_register)
      end

      it "should not send client event for local connenction" do
        client_register.client_connection.set_type('local')
        expect(subject).to_not receive(:send_client_event)
        subject.on_receive(client_register)
      end
    end

    describe "#unregister_sender" do
      it "sends disconnected message to message queue" do
        expect(subject).to receive(:send_client_event).with(client_name,player_id,'disconnected')
        subject.on_receive(client_unregister)
      end

      it "removes local client reference" do
        subject.local_clients[client_name] = true
        subject.on_receive(client_unregister)
        expect(subject.local_clients.has_key?(client_name)).to be_false
      end

      it "should not send client event for local connenction" do
        client_unregister.client_connection.set_type('local')
        expect(subject).to_not receive(:send_client_event)
        subject.on_receive(client_unregister)
      end
    end


  end

end
