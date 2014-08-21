
require_relative 'item_manager'
require_relative 'zone_manager'

GameMachine::Actor::Builder.new(Tutorial::ZoneManager).start

# For the inventory handler we use a distributed router.  The routing system for game messages uses
# the player id to hash against which guarantees that player ids are mapped to specific actors.
# This lets us cache player specific data in the actor that the player is hashed to.
if GameMachine::Application.config.orm
  require_relative 'seed'
  GameMachine::Actor::Builder.new(Tutorial::ItemManager).distributed(3).start
end


