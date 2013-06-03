require 'spec_helper'

module GameMachine
  describe ConnectionManager do
    before(:all) {Server.start}
    after(:all) {Server.stop}

    subject do
      props = Props.new(ConnectionManager);
      ref = TestActorRef.create(GameMachineLoader.get_actor_system, props, ConnectionManager.name);
      ref.underlying_actor
    end

  end
end
