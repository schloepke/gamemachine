
require_relative 'inventory_handler'

require_relative 'seed'

GameMachine::Routes.game_messages do

  # Incoming game messages have a destination that is either the integer id of the route, or the name.
  # Integer id's serve the purpose of keeping message size to a minimum and are recommended for production use.

  # The route id and the to: parameter are both required.  Everything else is optional
  route 1, to: Tutorial::InventoryHandler, distributed: true, name: 'inventory'
end

# For the inventory handler we use a distributed router.  The routing system for game messages uses
# the player id to hash against which guarantees that player ids are mapped to specific actors.
# This lets us cache player specific data in the actor that the player is hashed to.
if GameMachine::Application.config.orm
  GameMachine::Actor::Builder.new(Tutorial::InventoryHandler).distributed(3).start
end


