
require_relative 'tutorial_handler'
require_relative 'inventory_handler'

require_relative 'seed'

if GameMachine::Application.config.orm
  GameMachine::Actor::Builder.new(Tutorial::InventoryHandler).start
end

GameMachine::Actor::Builder.new(Tutorial::TutorialHandler).start
