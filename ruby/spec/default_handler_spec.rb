require 'spec_helper'

module GameMachine
  module Systems
    describe DefaultHandler do

      let(:entity) do
        entity = Entity.new
        entity.set_id(1)
        player = Player.new
        player.set_authtoken('authorized')
        player.set_id('2')
        entity.set_player(player)
        entity
      end

      let(:entities) do
        entities = EntityList.new
        entities.add_entity(entity)
        entities
      end

      let(:gateway_message) do
        GatewayMessage.new(entities.to_byte_array,'client1')
      end

      subject do
        props = JavaLib::Props.new(DefaultHandler);
        ref = JavaLib::TestActorRef.create(Server.instance.actor_system, props, 'commandrouter1');
        ref.underlying_actor
      end

      describe "#on_receive" do

        it "dispatches entities to the correct systems" do
          #ConnectionManager.should_receive(:tell).with(kind_of(GameMachine::Entity))
          #subject.onReceive(gateway_message)
        end
      end


    end
  end
end
