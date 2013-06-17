require 'spec_helper'

module GameMachine
  describe CommandRouter do

    let(:entity) do
      entity = Entity.new
      entity.set_id(1)
      player_connection = PlayerConnection.new
      player_connection.set_id('client1')
      player_connection.set_player(Player.new.set_id(1))
      entity.set_player_connection(player_connection)
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
      props = Props.new(CommandRouter);
      ref = TestActorRef.create(GameMachineLoader.get_actor_system, props, 'commandrouter1');
      ref.underlying_actor
    end

    describe "#on_receive" do

      it "dispatches entities to the correct systems" do
        ConnectionManager.should_receive(:tell).with(kind_of(Entity))
        subject.onReceive(gateway_message)
      end
    end


  end
end
