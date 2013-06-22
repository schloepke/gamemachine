require 'spec_helper'

module GameMachine
  describe ConnectionManager do

    subject do
      props = JavaLib::Props.new(ConnectionManager);
      ref = JavaLib::TestActorRef.create(GameMachineLoader.get_actor_system, props, ConnectionManager.name);
      ref.underlying_actor
    end

  end
end
