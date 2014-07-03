require 'spec_helper'

module GameMachine
  module Handlers
    describe Request do

      let(:player) {MessageLib::Player.new.set_id('1')}
      let(:client_id) {'client1'}
      let(:client_connection) {MessageLib::ClientConnection.new}

      let(:entity) do
       MessageLib::Entity.new.set_id('1')
      end

      let(:player_logout) do
        MessageLib::PlayerLogout.new.set_player_id(player.id)
      end

      let(:client_message) do
        MessageLib::ClientMessage.new.
          set_client_connection(client_connection).
          set_player(player).
          add_entity(entity)
      end

      let(:actor_ref) {double('Actor::Ref', :tell => true)}

      subject do
        ref = Actor::Builder.new(Request).with_name('request_test').test_ref
        ref.underlying_actor
      end

      describe "#on_receive" do

        it "sets player on entities" do
          allow(Authentication).to receive(:authenticated?).and_return(true)
          message = client_message
          entity = message.get_entity_list.first
          expect(entity).to receive(:set_player).with(message.player)
          subject.on_receive(message)
        end


        it "calls unhandled if not a client message" do
          expect(subject).to receive(:unhandled).with('test')
          subject.on_receive('test')
        end

      end


    end
  end
end
