require 'spec_helper'

module GameMachine
  describe CommandRouter do
    before(:all) {Server.start}
    after(:all) {Server.stop}

    let(:entity) do
      entity = Entity.new(1)
      client_connection = ClientConnection.new
      client_connection.set_id('client1')
      client_connection.set_connected(false)
      entity.set_client_connection(client_connection)
      entity
    end

    let(:entities) do
      entities = Entities.new
      entities.add_entity(entity)
      entities
    end

    let(:gateway_message) do
      GatewayMessage.new(Components.from_entities(entities).to_byte_array,'client1')
    end

    subject do
      props = Props.new(CommandRouter);
      ref = TestActorRef.create(GameMachineLoader.get_actor_system, props, CommandRouter.name);
      ref.underlying_actor
    end

    describe "Authentication" do
      it "should do something" do
       Gateway.should_receive(:send_to_client).and_return(true)
       ConnectionManager.should_receive(:tell).with(kind_of(Entity))
       subject.onReceive(gateway_message)
      end
    end


  end
end
