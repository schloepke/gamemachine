
require_relative 'message_router'
require_relative 'inventory_handler'

require_relative 'seed'

GameMachine::Routes.game_messages do

  route 'RequestPlayerItems', to: Tutorial::InventoryHandler, distributed: true, id: 0
  route 'AddPlayerItem', to: Tutorial::InventoryHandler, distributed: true, id: 1
  route 'RemovePlayerItem', to: Tutorial::InventoryHandler, distributed: true, id: 2
end

# game_message_handler in config.yml points to MessageRouter.
# We are using a router so it can process more then one message at once.
# Note that since we take care to not block in our code, even just 5 actors in the router pool
# can handle thousands of messages per second
GameMachine::Actor::Builder.new(Tutorial::MessageRouter).
  with_router(GameMachine::JavaLib::RoundRobinRouter,5).start

# For the inventory handler we use a distributed router.  We will use the player id to hash against, which
# will pin players to specific actors.  This lets us use local memory caching within actors.
if GameMachine::Application.config.orm
  GameMachine::Actor::Builder.new(Tutorial::InventoryHandler).distributed(3).start
end


