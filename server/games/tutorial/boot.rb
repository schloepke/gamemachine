
require_relative 'item_manager'

require_relative 'seed'

GameMachine::Routes.game_messages do

  # The route id and the to: parameter are both required.  Everything else is optional
  # route id's can be any positive integer.  For ruby actors, to: should be the class.  For java actors
  # to: should be the string name of the actor (as defined in the java class)
  route 1, to: Tutorial::ItemManager, distributed: true, name: 'item_manager'
  route 2, to: 'loot_generator', name: 'loot_generator'
end

# For the inventory handler we use a distributed router.  The routing system for game messages uses
# the player id to hash against which guarantees that player ids are mapped to specific actors.
# This lets us cache player specific data in the actor that the player is hashed to.
if GameMachine::Application.config.orm
  GameMachine::Actor::Builder.new(Tutorial::ItemManager).distributed(3).start
end


