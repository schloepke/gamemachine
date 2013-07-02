require 'spec_helper'

module GameMachine
  module Systems
    describe RequestHandler do

      let(:entity) do
        entity = Entity.new
        entity.set_id('1')
        player = Player.new
        player.set_authtoken('authorized')
        player.set_id('2')
        entity.set_player(player)
        entity
      end

      let(:client_request) do
        client_request = ClientRequest.new
        client_request.add_entity(entity)
        client_request
      end

      let(:gateway_message) do
        GatewayMessage.new(client_request.to_byte_array,'client1')
      end

      subject do
        props = JavaLib::Props.new(RequestHandler);
        ref = JavaLib::TestActorRef.create(Server.instance.actor_system, props, 'commandrouter1');
        ref.underlying_actor
      end

      describe "#on_receive" do

        it "dispatches client_request to the correct systems" do
          #ConnectionManager.should_receive(:tell).with(kind_of(GameMachine::Entity))
          #subject.onReceive(gateway_message)
        end
      end


    end
  end
end
